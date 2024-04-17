package org.example.dollarorder.feign;

import feign.FeignException.FeignClientException;
import org.example.dollarorder.domain.product.entity.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dollar-product", url = "https://product.10-trillon-dollars.com/external")
//@FeignClient(name = "dollar-product", url = "http://localhost:8083/external")
public interface ProductFeignClient {

    @GetMapping("/products/{productId}")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 5000)
        , noRetryFor = {FeignClientException.class}
    )
    Product getProduct(@PathVariable("productId") Long productId);

    @PostMapping("/products")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 5000)
        , noRetryFor = {FeignClientException.class}
    )
    void save(@RequestBody Product product);
}

