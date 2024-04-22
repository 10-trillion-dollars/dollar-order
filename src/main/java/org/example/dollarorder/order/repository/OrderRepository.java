package org.example.dollarorder.order.repository;


import java.time.LocalDateTime;
import java.util.List;
import org.example.dollarorder.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findOrdersByUserId(Long userId);
    //주문하고 5분 뒤에 결제 안한 주문 찾는 쿼리
    @Query("SELECT o FROM Order o WHERE o.state = 'NOTPAYED' AND o.createdAt < :cutoff")
    List<Order> findUnpaidOrdersOlderThan(@Param("cutoff") LocalDateTime cutoff);

    @Query("SELECT o FROM Order o WHERE o.id IN (:orderIdList)")
    List<Order> findAllByOrderId(List<Long> orderIdList);
}
