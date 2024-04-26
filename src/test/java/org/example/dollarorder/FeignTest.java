package org.example.dollarorder;

import org.assertj.core.api.Assertions;
import org.example.dollarorder.domain.address.entity.Address;
import org.example.dollarorder.domain.product.entity.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class FeignTest {

    //실행하기 전에 FeignClient에 url이 local인지 확인
    //각 서버가 실행이 되고 있는지 확인

    @Test
    @DisplayName("getProduct api 통신 테스트")
    public void ProductFeigntest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Product> response = restTemplate
                .getForEntity("http://localhost:8083/external/products/1", Product.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getBody());
    }

    @Test
    @DisplayName("findOne api 통신 테스트")
    public void AddressFeigntest() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Address> response = restTemplate
                .getForEntity("http://localhost:8082/external/address/1", Address.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response.getBody());
    }

}
