package org.example.dollarorder.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.dollarorder.order.entity.Order;
import org.example.dollarorder.order.entity.OrderState;


@Getter
@Setter
@AllArgsConstructor
@ToString
public class OrderResponseDto {
    private Long orderId;
    private OrderState state;
    private String address;

    public OrderResponseDto(Order order){
       this.orderId = order.getId();
       this.state = order.getState();
       this.address = order.getAddress().getCity()+" "+order.getAddress().getVillage()+" "+ order.getAddress().getProvince();
    }
}
