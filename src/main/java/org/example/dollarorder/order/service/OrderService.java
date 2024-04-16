package org.example.dollarorder.order.service;

import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dollarorder.domain.address.entity.Address;
import org.example.dollarorder.domain.product.entity.Product;
import org.example.dollarorder.feign.AddressFeignClient;
import org.example.dollarorder.feign.ProductFeignClient;
import org.example.dollarorder.order.dto.OrderDetailResponseDto;
import org.example.dollarorder.order.dto.OrderResponseDto;
import org.example.dollarorder.order.entity.Order;
import org.example.dollarorder.order.entity.OrderDetail;
import org.example.dollarorder.order.entity.OrderState;
import org.example.dollarorder.order.repository.OrderDetailRepository;
import org.example.dollarorder.order.repository.OrderRepository;
import org.example.share.config.global.exception.BadRequestException;
import org.example.share.config.global.security.UserDetailsImpl;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductFeignClient productService;
    private final AddressFeignClient addressService;
    private final RedissonClient redissonClient;

    @Transactional
    public void createOrder(Map<Long, Long> basket, UserDetailsImpl userDetails, Long addressId)
        throws Exception {
        checkBasket(basket);
        Order order = new Order(userDetails.getUser().getId(), OrderState.NOTPAYED, addressId);
        order = orderRepository.save(order); // 저장된 order 객체를 다시 할당하여 ID를 포함하도록 함

        for (Long productId : basket.keySet()) {
            String lockKey = "product_lock:" + productId;
            RLock lock = redissonClient.getLock(lockKey);
            try {
                boolean isLocked = lock.tryLock(5, 10, TimeUnit.SECONDS);
                if (!isLocked) {
                    throw new RuntimeException("락 획득에 실패했습니다.");
                }
                Product product = productService.getProduct(productId);
                if (product.getStock() < basket.get(productId)) {
                    throw new BadRequestException("상품 재고가 부족합니다. 상품 ID: " + productId);
                }
                OrderDetail orderDetail = new OrderDetail(order.getId(), productId, basket.get(productId),
                    product.getPrice(), product.getName());
                orderDetailRepository.save(orderDetail);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 다시 설정
                throw new RuntimeException("락 획득 중 오류가 발생했습니다.", e);
            } catch (Exception e) {
                log.error("결제 과정에서 예상치 못한 오류가 발생했습니다.", e.getMessage());
                throw e;
            }
            finally {
                if (lock.isLocked()) {
                    lock.unlock();
                }
            }
        }
    }
    @Transactional
    public void updateStockAndCreateOrderDetail(Long productId, Long quantity) {
        Product product = productService.getProduct(productId);
        System.out.println(product.getStock());
        Long stock = product.getStock();
        // 재고 확인
        if (quantity > stock) {
            throw new BadRequestException("상품 재고가 부족합니다. 상품 ID: " + productId);
        }
        product.updateStockAfterOrder(quantity);
        productService.save(product);
    }

    public List<OrderDetailResponseDto> getOrderDetailList(Long orderId) {
        List<OrderDetail> listOfOrderedProducts = orderDetailRepository.findOrderDetailsByOrderId(
            orderId);
        return listOfOrderedProducts.stream().map(OrderDetailResponseDto::new).toList();
    }


    @Transactional
    public void deleteOrder(Long orderId) {
        orderDetailRepository.deleteAll(
            orderDetailRepository.findOrderDetailsByOrderId(orderId));
        orderRepository.delete(orderRepository.getById(orderId));
    }

    public boolean checkUser(UserDetailsImpl userDetails, Long orderId) {
        return Objects.equals(userDetails.getUser().getId(),
            orderRepository.getById(orderId).getUserId());
    }

    public boolean checkStock(Long productId, Long quantity) {
        Long stock = productService.getProduct(productId).getStock();
        if (stock < 1) {
            throw new RuntimeException();
        }
        return stock - quantity >= 0;
    }

    public List<OrderResponseDto> getOrderList(UserDetailsImpl userDetails) {
        List<Order> orderList = orderRepository.findOrdersByUserId(userDetails.getUser().getId());
        List<OrderResponseDto> ResponseList = new ArrayList<OrderResponseDto>();
        for (Order order : orderList) {
            Address address = addressService.findOne(order.getAddressId());
            OrderResponseDto orderResponseDto = new OrderResponseDto(order, address);
            ResponseList.add(orderResponseDto);
        }
        return ResponseList;
    }

    public void checkBasket(Map<Long, Long> basket) throws Exception {
        for (Long key : basket.keySet()) {
            if (!checkStock(key, basket.get(key))) {
                throw new Exception("id:" + key + " 수량부족");
            }
        }
    }

    public Long getTotalPrice(Long orderId) {
        List<OrderDetail> ListofOrderDetail = orderDetailRepository.findOrderDetailsByOrderId(
            orderId);
        Long totalPrice = 0L;
        for (OrderDetail orderDetail : ListofOrderDetail) {
            totalPrice += orderDetail.getPrice() * orderDetail.getQuantity();
        }
        return totalPrice;
    }

    public List<OrderDetail> getOrderDetails(Long userId, Long productId) {
        return orderDetailRepository.findByUserIdAndProductIdAndReviewedIsFalse(userId, productId);
    }

    @Transactional
    public void saveOrderDetailReviewedState(OrderDetail orderDetail){
        orderDetail.setReviewed(true);
        orderDetailRepository.save(orderDetail);
    }


    @Transactional
    public Long createOrderTest(Map<Long,Long> basket,UserDetailsImpl userDetails,Long addressId) throws Exception {
        checkBasket(basket);
        Order order = new Order(userDetails.getUser().getId(),OrderState.NOTPAYED, addressId);
        orderRepository.save(order);
        for(Long key:basket.keySet()){
            OrderDetail orderDetail= new OrderDetail(order.getId(),key,basket.get(key),productService.getProduct(key).getPrice(),productService.getProduct(key).getName());
            orderDetailRepository.save(orderDetail);
            updateStock(key,basket.get(key));
        }
        return order.getId();
    }

    public void updateStock(Long productId,Long quantity) throws ChangeSetPersister.NotFoundException {
        Product product =  productService.getProduct(productId);
        product.updateStockAfterOrder(quantity);
    }

    public Order getById(Long orderId){
        return orderRepository.findById(orderId).orElseThrow();
    }

}

