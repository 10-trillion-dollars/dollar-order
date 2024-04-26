package org.example.dollarorder.domain.address.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@Entity
@Table(name = "address")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //시,도
    @Column(nullable = false)
    private String city;

    //구
    @Column(nullable = false)
    private String village;

    //동
    @Column(nullable = false)
    private String province;

    @Column
    private Long userId;

}
