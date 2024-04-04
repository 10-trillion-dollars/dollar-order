package org.example.dollarorder.kakaopay.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dollarorder.kakaopay.dto.request.CancelRequestDto;
import org.example.dollarorder.kakaopay.dto.request.PayInfoDto;
import org.example.dollarorder.kakaopay.dto.request.PayRequestDto;
import org.example.dollarorder.kakaopay.dto.response.CancelResDto;
import org.example.dollarorder.kakaopay.dto.response.PayApproveResDto;
import org.example.dollarorder.kakaopay.dto.response.PayReadyResDto;
import org.example.dollarorder.order.entity.OrderState;
import org.example.dollarorder.order.repository.OrderRepository;
import org.example.dollarorder.order.service.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Service
@Slf4j
public class KakaoPayService {
    private final MakeRequest makeRequest;
    private final OrderRepository orderRepository;
    private final OrderService orderService;

    @Value("${kakao.api.admin-key}")
    private String adminKey;



    @Transactional
    public PayReadyResDto getRedirectUrl(Long orderId)throws Exception{

        HttpHeaders headers=new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization",auth);
        PayRequestDto payRequestDto = makeRequest.getReadyRequest(createPayInfo(orderId));
        HttpEntity<MultiValueMap<String, String>> urlRequest = new HttpEntity<>(payRequestDto.getMap(), headers);
        RestTemplate rt = new RestTemplate();
        PayReadyResDto payReadyResDto = rt.postForObject(payRequestDto.getUrl(), urlRequest, PayReadyResDto.class);
        orderRepository.getReferenceById(orderId).updateTid(payReadyResDto.getTid());
        return payReadyResDto;
    }

    @Transactional
    public PayApproveResDto getApprove(String pgToken, Long orderId)throws Exception{

        String tid= orderRepository.getReferenceById(orderId).getKakaoTid();

        HttpHeaders headers=new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization",auth);

        PayRequestDto payRequestDto = makeRequest.getApproveRequest(tid,pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(payRequestDto.getMap(), headers);

        RestTemplate rt = new RestTemplate();
        PayApproveResDto payApproveResDto = rt.postForObject(payRequestDto.getUrl(), requestEntity, PayApproveResDto.class);
        orderRepository.getReferenceById(orderId).changeState(OrderState.PREPARING);
        return payApproveResDto;
    }
    public CancelResDto kakaoCancel(Long orderId){

        String tid= orderRepository.getReferenceById(orderId).getKakaoTid();

        HttpHeaders headers=new HttpHeaders();
        String auth = "KakaoAK " + adminKey;
        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization",auth);

        CancelRequestDto cancelRequestDto = makeRequest.getCancelRequest(tid,orderService.getTotalPrice(orderId));

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(cancelRequestDto.getMap(), headers);
        RestTemplate rt = new RestTemplate();
         CancelResDto cancelResDto = rt.postForObject(cancelRequestDto.getUrl(),requestEntity,CancelResDto.class);
         return cancelResDto;
    }

    public PayInfoDto createPayInfo(Long orderId){
        PayInfoDto payInfoDto = new PayInfoDto();
        payInfoDto.setPrice(orderService.getTotalPrice(orderId));
        payInfoDto.setItemName("TenCompany");
        return payInfoDto;
    }




}