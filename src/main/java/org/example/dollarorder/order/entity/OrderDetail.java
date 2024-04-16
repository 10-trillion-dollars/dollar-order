package org.example.dollarorder.order.entity;


import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_detail")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long productId;

    @Column
    private String productName;

    @Column
    private Long price;

    @Column
    private Long quantity;

    @Column(nullable = false)
    private boolean reviewed = false;

    @Column
    private Long orderId;

    public OrderDetail(Long orderId, Long productId, Long quantity, Long price, String productName) {
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.orderId = orderId;
        this.price = price;
    }

}