package org.example.dollarorder.order.dto;

import java.util.Map;
import lombok.Getter;
@Getter
public class OrderRequestDto {

    private Map<Long,Long> basket;
    private Long addressId;
}
