package org.example.dollarorder.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dollarorder.global.TimeStamped;
import org.hibernate.annotations.BatchSize;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "orders")
@BatchSize(size = 10) // 한 번에 10개의 제품을 로딩하도록 설정
public class Order extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(value = EnumType.STRING)
    private OrderState state;

    @Column
    private Long userId;

    @Column
    private Long addressId;

    @Column
    private String KakaoTid;

    public Order(Long userId, OrderState state, Long addressId) {
        this.userId = userId;
        this.state = state;
        this.addressId = addressId;
    }

    public void changeState(OrderState state) {
        this.state = state;
    }

    public void updateTid(String tid) {
        this.KakaoTid = tid;
    }

}
