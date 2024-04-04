package org.example.dollarorder.order.controller;

import lombok.RequiredArgsConstructor;

import org.example.dollarorder.order.dto.CommonResponseDto;
import org.example.dollarorder.order.dto.StateRequestDto;
import org.example.dollarorder.order.service.OrderAdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class OrderAdminController {

    private final OrderAdminService orderAdminService;
    @Secured("ROLE_SELLER")
    @PutMapping("/order/{orderId}")
    public ResponseEntity<CommonResponseDto> updateOrderState(@RequestBody StateRequestDto requestDto, @PathVariable Long orderId){
        orderAdminService.changeState(requestDto.getStateNum(),orderId);
        return ResponseEntity.status(200).body(new CommonResponseDto(200,"변경이 완료됐습니다."));
    }

}
