package org.example.dollarorder.order.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final EmailService emailService;

    public void notifyStockUpdate(Long productId, String productName) {
        emailService.nofityStockUpdate(productId, productName);
    }
}
