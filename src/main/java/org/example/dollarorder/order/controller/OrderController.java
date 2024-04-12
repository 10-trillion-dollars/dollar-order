package org.example.dollarorder.order.controller;


import java.util.List;
import lombok.RequiredArgsConstructor;
import org.example.dollarorder.order.dto.CommonResponseDto;
import org.example.dollarorder.order.dto.OrderDetailResponseDto;
import org.example.dollarorder.order.dto.OrderRequestDto;
import org.example.dollarorder.order.dto.OrderResponseDto;
import org.example.dollarorder.order.service.OrderService;
import org.example.share.config.global.security.UserDetailsImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("")
    public ResponseEntity<CommonResponseDto> makeOrder(@RequestBody OrderRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        orderService.createOrder(requestDto.getBasket(),userDetails, requestDto.getAddressId());
        return ResponseEntity.status(200).body(new CommonResponseDto(200,"주문이 완료됐습니다."));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<List<OrderDetailResponseDto>> getOrder(@PathVariable Long orderId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(orderService.checkUser(userDetails,orderId)){
            return ResponseEntity.status(200).body(orderService.getOrderDetailList(orderId));}
        throw new RuntimeException("접속 권한 없음");
    }
    @GetMapping("/userorder")
    public ResponseEntity<List<OrderResponseDto>> getUserOrder(@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.status(200).body(orderService.getOrderList(userDetails));}


    @DeleteMapping("/{orderId}")
    public ResponseEntity<CommonResponseDto> cancelOrder(@PathVariable Long orderId,@AuthenticationPrincipal UserDetailsImpl userDetails) {
        if (orderService.checkUser(userDetails,orderId)) {
            orderService.deleteOrder(orderId);
            return ResponseEntity.status(200).body(new CommonResponseDto(200, "주문을 취소했습니다"));
        }
        throw new RuntimeException("접속 권한 없음");
    }


}


