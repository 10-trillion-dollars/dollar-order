
package org.example.dollarorder.kakaopay.controller;

import lombok.RequiredArgsConstructor;

import org.example.dollarorder.kakaopay.dto.response.CancelResDto;
import org.example.dollarorder.kakaopay.dto.response.PayApproveResDto;
import org.example.dollarorder.kakaopay.service.KakaoPayService;
import org.example.dollarorder.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payment")
public class KakaoPayController {

    private final KakaoPayService kakaoPayService;

    @GetMapping("/ready/{orderId}")
    public ResponseEntity<?> getRedirectUrl(@PathVariable Long orderId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
            .body(kakaoPayService.getRedirectUrl(orderId));
    }

    @GetMapping("/success/{orderId}")
    public ResponseEntity<?> afterGetRedirectUrl(@PathVariable Long orderId,
        @RequestParam("pg_token") String pgToken) throws Exception {
        PayApproveResDto kakaoApprove = kakaoPayService.getApprove(pgToken,orderId);
        return ResponseEntity.status(HttpStatus.OK)
            .body("결제가 완료되었습니다 해당 페이지를 종료해주십시요.");
    }

    @GetMapping("/cancel/{orderId}")
    public ResponseEntity<?> cancel(@PathVariable Long orderId) throws Exception {
        CancelResDto cancelResDto = kakaoPayService.kakaoCancel(orderId);
        return ResponseEntity.status(HttpStatus.OK)
            .body(cancelResDto);
    }

}

