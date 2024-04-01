package org.example.dollarorder.order.repository;


import java.util.List;
import org.example.dollarorder.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.example.dollarorder.order.entity.Order;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findOrderDetailsByOrder(Order order);
    //review 검증 jpal
    @Query("SELECT COUNT(od) FROM OrderDetail od WHERE od.order.user.id = :userId AND od.productId = :productId")
    long countByUserIdAndProductId(@Param("userId") Long userId, @Param("productId") Long productId);

}
