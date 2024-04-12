package org.example.dollarorder.order.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dollarorder.order.entity.OrderDetail;
import org.example.dollarorder.order.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/external")
public class OrderFeignController {
    private final OrderService orderService;
    @GetMapping("/users/{userId}/products/{productId}")
    Long countByUserIdAndProductId(@PathVariable Long userId, @PathVariable Long productId){
        return orderService.countByUserIdAndProductId(userId, productId);
    }

    @GetMapping("/users/{userId}/products/{productId}/orders")
    List<OrderDetail> getOrderDetails(@PathVariable Long userId, @PathVariable Long productId){
        return orderService.getOrderDetails(userId, productId);
    }

    @PostMapping("/orders/orderDetail/reviewState")
    void saveOrderDetailReviewedState(OrderDetail orderDetail){
        orderService.saveOrderDetailReviewedState(orderDetail);
    }

}
