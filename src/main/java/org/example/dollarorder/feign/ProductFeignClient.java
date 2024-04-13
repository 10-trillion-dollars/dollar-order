package org.example.dollarorder.feign;

import org.example.dollarorder.domain.product.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dollar-product", url = "http://localhost:8083/external")
public interface ProductFeignClient {

    @GetMapping("/products/{productId}")
    Product getProduct(@PathVariable("productId") Long productId);

    @PostMapping("/products")
    void save(@RequestBody Product product);
}

