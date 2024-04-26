package org.example.dollarorder.order.repository;

import java.sql.PreparedStatement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dollarorder.order.entity.OrderDetail;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
public class OrderDetailBulkRepository {

    private final JdbcTemplate jdbcTemplate;

    @Transactional
    public void saveBulk(List<OrderDetail> orderDetailList) {
        String sql = "INSERT INTO order_detail (product_id, product_name, price, quantity, reviewed, order_id)" +
            "VALUES (?, ?, ?, ?, ?, ?)";

        jdbcTemplate.batchUpdate(sql,
            orderDetailList,
            orderDetailList.size(),
            (PreparedStatement ps, OrderDetail orderDetail) -> {
                ps.setLong(1, orderDetail.getProductId());
                ps.setString(2, orderDetail.getProductName());
                ps.setLong(3, orderDetail.getPrice());
                ps.setLong(4, orderDetail.getQuantity());
                ps.setBoolean(5, orderDetail.isReviewed());
                ps.setLong(6, orderDetail.getOrderId());
            });
    }
}
