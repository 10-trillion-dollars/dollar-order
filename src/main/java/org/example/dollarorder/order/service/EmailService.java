package org.example.dollarorder.order.service;


import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final SesClient sesClient;
    private final RedissonClient redissonClient;

    public enum EmailType {
        STOCK_OUT,
        PAYMENT_TIMEOUT,
        STOCK_UPDATE
    }

    public void sendCancellationEmail(
        String recipientEmail,
        String orderDetails,
        EmailType emailType
    ) {
        //이메일 보내는 사람의 주소
        String sender = "team10testsparta@gmail.com"; // 검증된 발신자 이메일
        String htmlBody;
        String subject;
        // 상황에 따른 이메일 폼
        switch (emailType) {
            case STOCK_OUT:
                htmlBody = buildStockOutCancellationHtml(orderDetails);
                subject = "주문 취소 안내";
                break;
            case PAYMENT_TIMEOUT:
                htmlBody = buildPaymentTimeoutCancellationHtml(orderDetails);
                subject = "결제 시간 초과로 인한 주문 취소";
                break;
            case STOCK_UPDATE:
                htmlBody = buildStockUpdateHtml(orderDetails);
                subject = "재고 업데이트 알림";
                break;
            default:
                throw new IllegalArgumentException("Unknown email type");
        }
        //이메일 요청 객체 빌더 초기화
        SendEmailRequest request = SendEmailRequest.builder()
            //이메일 수신자 설정
            .destination(Destination.builder().toAddresses(recipientEmail).build())
            //이메일 내용 구성
            .message(Message.builder()
                //이메일 제목 생성
                .subject(Content.builder().data(subject).build())
                //이메일 내용 구성
                .body(Body.builder().text(Content.builder().data(htmlBody).build()).build())
                //Massage 객체 빌드 완료
                .build())
            //발송자 설정 완료
            .source(sender)
            //sandMailRequest 빌드 완료
            .build();
        //SES 클라이언트를 통해 이메일 전송
        sesClient.sendEmail(request);
        //이메일 전송 성공 로그 출력
        System.out.println("Cancellation email sent to " + recipientEmail);
    }
    //이메일 템플릿 1) 재고가 부족할 경우
    private String buildStockOutCancellationHtml(String orderDetails) {
        return "<html><body>"
            + "<h1>안녕하세요 10-trillon-dollars 입니다. 주문이 취소 되었습니다.</h1>"
            + "<p>안타깝게도 재고 부족으로 인해 다음 품목에 대한 최근 주문이 취소되었습니다</p>"
            + "<p><b>Order Details:</b> " + orderDetails + "</p>"
            + "</body></html>";
    }
    //이메일 템플릿 2) 결제를 5분동안 안해서 주문이 취소된 경우
    private String buildPaymentTimeoutCancellationHtml(String orderDetails) {
        return "안녕하세요 10-trillon-dollars 입니다. 주문 상품" + orderDetails +
            "의 주문이 취소 되었습니다.\n지정된 기간 내에 결제가 이루어지지 않아 주문이 취소되었습니다.";
    }
    //이메일 템플릿 3) 재고가 업데이트 될 경우
    private String buildStockUpdateHtml(String productDetails) {
        return "<html><body>"
            + "<h1>안녕하세요 10-trillon-dollars 입니다. 재고가 업데이트 되었습니다.</h1>"
            + "<p>재고 부족으로 인해 취소되었던 상품의 재고가 업데이트되었습니다. 주문은 선착순으로 처리됩니다.</p>"
            + "<p><b>Product Details:</b> " + productDetails + "</p>"
            + "</body></html>";
    }
    //재고 부족으로 인한 주문 취소 고객 저장 메서드 (레디스 사용)
    public void saveStock_Out_UserInfoToRedis(String email, Long productId) {
        String key = "stockOut:" + productId;
        //재고 업데이트를 받을 사용자 이메일 저장
        redissonClient.getList(key).add(email);
        //재고 업데이트 알림 유지 기간 설정, 24시간
        redissonClient.getBucket(key).expire(10, TimeUnit.MINUTES);
    }

    //재고 업데이트 시 레디스 조회 및 이메일 전송
    public void nofityStockUpdate(Long productId,String productName) {
        // 레디스 키 생성
        String key = "stockOut:" + productId;
        // 레디스에서 이 키에 해당하는 메일 리스트를 가져옴
        RList<String> emails = redissonClient.getList(key);
        if (!emails.isEmpty()) {
            String productDetails = productName;
            for (String email : emails) {
                sendCancellationEmail(email, productDetails, EmailType.STOCK_UPDATE);
            }
            redissonClient.getKeys().delete(key);
        } else {
            System.out.println("해당되는 상품이 없습니다" + productId);
        }
    }

    @PreDestroy
    public void close() {
        if (sesClient != null) {
            sesClient.close();
            System.out.println("SES Client has been closed.");
        }
    }

}












