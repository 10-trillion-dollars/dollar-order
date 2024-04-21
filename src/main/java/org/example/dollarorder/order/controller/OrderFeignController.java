package org.example.dollarorder.order.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dollarorder.order.entity.Order;
import org.example.dollarorder.order.entity.OrderDetail;
import org.example.dollarorder.order.service.NotificationService;
import org.example.dollarorder.order.service.OrderAdminService;
import org.example.dollarorder.order.service.OrderService;
import org.springframework.http.ResponseEntity;
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
    private final NotificationService notificationService;
//    @GetMapping("/users/{userId}/products/{productId}")
//    Long countByUserIdAndProductId(@PathVariable Long userId, @PathVariable Long productId){
//        return orderService.countByUserIdAndProductId(userId, productId);
//    }

    @GetMapping("/orders/{orderId}")
    Order getById(@PathVariable Long orderId) {
        return orderService.getById(orderId);
    }

    @GetMapping("/users/{userId}/products/{productId}/orders")
    List<OrderDetail> getOrderDetails(@PathVariable Long userId, @PathVariable Long productId) {
        return orderService.getOrderDetails(userId, productId);
    }

    @PostMapping("/orders/orderDetail/reviewState")
    void saveOrderDetailReviewedState(@RequestBody OrderDetail orderDetail) {
        orderService.saveOrderDetailReviewedState(orderDetail);
    }

    @GetMapping("/{productId}/orderDetails")
    List<OrderDetail> findOrderDetailsByProductId(@PathVariable Long productId) {
        return orderAdminService.findOrderDetailsByProductId(productId);
    }
    @GetMapping("/notify-stock-update/{productId}/{productName}")
    public ResponseEntity<String> notifyStockUpdate(
        @PathVariable Long productId,
        @PathVariable String productName) {
        notificationService.notifyStockUpdate(productId, productName);
        return ResponseEntity.ok("Stock update notification sent successfully");
    }
}
