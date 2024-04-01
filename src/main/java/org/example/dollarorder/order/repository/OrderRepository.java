package org.example.dollarorder.order.repository;


import java.util.List;
import org.example.dollarorder.order.entity.Order;
import org.example.dollarorder.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUser(User user);
}
