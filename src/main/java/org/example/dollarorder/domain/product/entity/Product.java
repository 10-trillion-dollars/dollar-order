package org.example.dollarorder.domain.product.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.dollarorder.global.TimeStamped;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;
    @Column
    private Long price;
    @Column
    private String description;
    @Column
    private Long stock;
    @Column
    private String photo;
    @Column
    private boolean state;
    @Column
    private Long userId;

    public void updateStockAfterOrder(Long quantity) {
        this.stock = stock - quantity;
    }

}
