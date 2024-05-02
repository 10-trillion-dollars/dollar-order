package org.example.dollarorder.feign;

import java.util.List;
import java.util.Set;
import org.example.dollarorder.domain.address.entity.Address;

import org.example.share.config.global.entity.user.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@FeignClient(name = "dollar-user", url = "${loadbalancer.user}/external")
//@FeignClient(name = "dollar-user", url = "http://localhost:8082/external")
public interface AddressFeignClient {
    @GetMapping("/address/{addressId}")
    Address findOne(@PathVariable Long addressId);
    @GetMapping("/users/{userId}")
    User getUser(@PathVariable("userId") Long userId);

    @PostMapping("/address/addressIdList")
    List<Address> findAddressListByAddressIdList(Set<Long> addressIdSet);
}
