package org.example.dollarorder.feign;

import feign.FeignException;
import feign.FeignException.FeignClientException;
import java.util.List;
import java.util.Map;
import org.example.dollarorder.domain.product.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "dollar-product", url = "${loadbalancer.product}/external")
//@FeignClient(name = "dollar-product", url = "http://localhost:8083/external")
public interface ProductFeignClient {

    @GetMapping("/products/{productId}")
    Product getProduct(@PathVariable("productId") Long productId);

    @PostMapping("/products")
    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 1000, maxDelay = 5000)
        , noRetryFor = {FeignClientException.class}
        , recover = "recoverSaveProduct"
    )
    void save(@RequestBody Product product);

    @Recover
    default void recoverSaveProduct(FeignException e) {
        Logger logger = LoggerFactory.getLogger(ProductFeignClient.class);
        logger.error("All retries failed., error = {}", e.getMessage());
    }


    @PostMapping("/products/saveBulk")
    void saveBulk(@RequestBody List<Product> productList);

    @PostMapping("/products/updateBulk")
    void updateStockAfterOrder(@RequestBody Map<Long, Long> basket);
}