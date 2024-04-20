package org.example.dollarorder.order.repository;


import java.util.List;
import org.example.dollarorder.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.example.dollarorder.order.entity.Order;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByProductId(Long productId);

    @Query("SELECT od FROM OrderDetail od WHERE od.orderId IN (SELECT o.id FROM Order o WHERE o.userId = :userId AND o.state = 'DELIVERED') AND od.productId = :productId AND od.reviewed = false")
    List<OrderDetail> findByUserIdAndProductIdAndReviewedIsFalse(@Param("userId") Long userId, @Param("productId") Long productId);

    List<OrderDetail> findByOrderId(Long orderId);

    List<OrderDetail> findOrderDetailsByOrderId(Long orderId);
}
