package org.example.dollarorder.order.controller;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.example.dollarorder.order.entity.Order;
import org.example.dollarorder.order.entity.OrderDetail;
import org.example.dollarorder.order.service.OrderAdminService;
import org.example.dollarorder.order.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
public class OrderFeignController {

    private final OrderService orderService;
    private final OrderAdminService orderAdminService;
//    @GetMapping("/users/{userId}/products/{productId}")
//    Long countByUserIdAndProductId(@PathVariable Long userId, @PathVariable Long productId){
//        return orderService.countByUserIdAndProductId(userId, productId);
//    }

//    @GetMapping("/orders/{orderId}")
//    Order getById(@PathVariable Long orderId) {
//        return orderService.getById(orderId);
//    }

    @PostMapping("/orders")
    Map<Long, Order> getAllById(@RequestBody List<Long> orderIdList) {
        return orderService.getAllById(orderIdList);
    }



    @GetMapping("/users/{userId}/products/{productId}/orders")
    List<OrderDetail> getOrderDetails(@PathVariable Long userId, @PathVariable Long productId) {
        return orderService.getOrderDetails(userId, productId);
    }

    @PostMapping("/orders/orderDetail/reviewState")
    void saveOrderDetailReviewedState(@RequestBody OrderDetail orderDetail) {
        orderService.saveOrderDetailReviewedState(orderDetail);
    }

    @PostMapping("/productLists/orderDetails")
    List<OrderDetail> findOrderDetailsByProductId(@RequestBody List<Long> productIdList) {
        return orderAdminService.findOrderDetailsByProductId(productIdList);
    }

//    @GetMapping("/{productId}/orderDetails")
//    List<OrderDetail> XfindOrderDetailsByProductId(@PathVariable Long productId) {
//        return orderAdminService.XfindOrderDetailsByProductId(productId);
//    }

}
