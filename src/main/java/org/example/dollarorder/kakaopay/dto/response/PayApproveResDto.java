package org.example.dollarorder.kakaopay.dto.response;

import lombok.Getter;

@Getter
public class PayApproveResDto {
    private Amount amount;
    private String item_name;
    private String created_at;
    private String approved_at;
    private Long orderId;
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}