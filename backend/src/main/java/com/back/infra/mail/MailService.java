package com.back.infra.mail;

import com.back.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender sender;

    public void sendOrderConfirmation(PaymentCompletedEvent e) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(e.buyerEmail());
        msg.setSubject("[Cafe] 결제 완료 영수증 #" + e.paymentId());

        StringBuilder body = new StringBuilder();
        body.append("안녕하세요, 고객님.\n")
                .append("주문이 정상적으로 결제되었습니다.\n\n")
                .append("주문번호 : ").append(e.orderId()).append('\n')
                .append("결제번호 : ").append(e.paymentId()).append('\n')
                .append("결제수단 : ").append(e.paymentMethod()).append('\n')
                .append("결제금액 : ").append(fmt(e.paidAmount())).append('\n')
                .append("결제시간 : ").append(e.paidAt()).append("\n\n")
                .append("주소     : ").append(e.address()).append("\n")
                .append("[구매 내역]\n");

        for (var l : e.lines()) {
            body.append("- ").append(l.productName())
                    .append(" x ").append(l.quantity())
                    .append(" (단가 ").append(fmt(l.unitPrice()))
                    .append(", 소계 ").append(fmt(l.lineAmount()))
                    .append(")\n");
        }

        body.append("\n감사합니다.");
        msg.setText(body.toString());
        sender.send(msg);
    }

    private String fmt(long won) { return String.format("%,d원", won); }
}
