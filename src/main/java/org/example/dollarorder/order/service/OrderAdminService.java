package org.example.dollarorder.order.service;

import lombok.RequiredArgsConstructor;

import org.example.dollarorder.order.entity.Order;
import org.example.dollarorder.order.entity.OrderState;
import org.example.dollarorder.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class OrderAdminService {

    private final OrderRepository orderRepository;
    public void changeState(int requestState,Long orderId) {
        Order order = orderRepository.getReferenceById(orderId);
        if (requestState == 0) {
            order.changeState(OrderState.PREPARING);
        } else if (requestState == 1) {
            order.changeState(OrderState.SHIPPING);
        } else if (requestState == 2) {
            order.changeState(OrderState.DELIVERED);
        } else if (requestState == 3) {
            order.changeState(OrderState.NOTPAYED);
        }
        orderRepository.save(order);

    }
}
