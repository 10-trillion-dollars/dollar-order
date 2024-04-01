package org.example.dollarorder.order.service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

import org.example.dollarorder.domain.address.repository.AddressRepository;
import org.example.dollarorder.global.security.UserDetailsImpl;
import org.example.dollarorder.order.dto.OrderDetailResponseDto;
import org.example.dollarorder.order.dto.OrderResponseDto;
import org.example.dollarorder.order.entity.Order;
import org.example.dollarorder.order.entity.OrderDetail;
import org.example.dollarorder.order.entity.OrderState;
import org.example.dollarorder.order.repository.OrderDetailRepository;
import org.example.dollarorder.order.repository.OrderRepository;
import org.example.dollarorder.domain.product.entity.Product;
import org.example.dollarorder.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    public Order createOrder(UserDetailsImpl userDetails,Long addressId){
        Order order = new Order(userDetails.getUser(), OrderState.PREPARING,addressRepository.getReferenceById(addressId));
        return orderRepository.save(order);
    }

    @Transactional
    public void saveOrderDetails(Map<Long,Long> basket,Order order) throws Exception {
        for(Long key:basket.keySet()){
            if(!CheckStock(key,basket.get(key))){throw new Exception("id:"+key+" 수량부족");}
        }
        for(Long key:basket.keySet()){
            OrderDetail orderDetail= new OrderDetail(order,key,basket.get(key),productRepository.getReferenceById(key).getPrice(),productRepository.getReferenceById(key).getName());
            orderDetailRepository.save(orderDetail);
            updateStock(key,basket.get(key));
        }

    }
    public List<OrderDetailResponseDto> getOrderDetailList(Long orderId){
        List<OrderDetail> listOfOrderedProducts = orderDetailRepository.findOrderDetailsByOrder(orderRepository.getReferenceById(orderId));
        return listOfOrderedProducts.stream().map(OrderDetailResponseDto::new).toList();
    }
    @Transactional
    public void deleteOrder(Long orderId){
        orderDetailRepository.deleteAll(orderDetailRepository.findOrderDetailsByOrder(orderRepository.getReferenceById(orderId)));
        orderRepository.delete(orderRepository.getReferenceById(orderId));
    }

    public boolean checkUser(UserDetailsImpl userDetails,Long orderId){
        return Objects.equals(userDetails.getUser().getId(), orderRepository.getReferenceById(orderId).getUser().getId());
    }

    public void updateStock(Long productId,Long quantity){
        Product product =  productRepository.getReferenceById(productId);
        product.updateStockAfterOrder(quantity);

    }

    public boolean CheckStock(Long productId,Long quantity){
        return productRepository.getReferenceById(productId).getStock() - quantity >= 0;
    }

    public List<OrderResponseDto> getOrderList(UserDetailsImpl userDetails){
        List<Order> orderList = orderRepository.findOrdersByUser(userDetails.getUser());
        return orderList.stream().map(OrderResponseDto::new).toList();
    }

}
