package org.example.dollarorder.feign;

import org.example.dollarorder.domain.address.entity.Address;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

 @FeignClient(name = "dollar-user", url = "https://user.10-trillon-dollars.com/external")
//@FeignClient(name = "dollar-user", url = "http://localhost:8082/external")
public interface AddressFeignClient {

    @GetMapping("/address/{addressId}")
    Address findOne(@PathVariable Long addressId);

}
