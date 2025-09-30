package com.back.infra.mail;

import com.back.domain.payment.event.PaymentCompletedEvent;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender sender;

    @Value("${app.mail.from:}")
    @Nullable
    private String from;

    private static final DateTimeFormatter DT_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final NumberFormat WON_FMT = NumberFormat.getNumberInstance(Locale.KOREA);

    public void sendOrderConfirmation(PaymentCompletedEvent e) {
        try {
            String html = renderReceiptHtml(e);

            MimeMessage mm = sender.createMimeMessage();
            MimeMessageHelper h = new MimeMessageHelper(mm, "UTF-8");

            if (from != null && !from.isBlank()) {
                h.setFrom("prdc.team01@gmail.com", "Cafe");
            }
            h.setTo(e.buyerEmail());
            h.setSubject("[Cafe] 결제 완료 영수증 #" + e.paymentId());
            h.setText(html, true);

            sender.send(mm);
        } catch (MessagingException | IOException ex) {
            throw new RuntimeException("이메일 전송 실패", ex);
        }
    }

    private String renderReceiptHtml(PaymentCompletedEvent e) throws IOException {
        String template = readReceiptTemplate("templates/mail/receipt.html");

        String paidAmount = fmtWon(e.paidAmount());
        String paidAt = formatDate(e.paidAt());

        StringBuilder rows = new StringBuilder();
        e.lines().forEach(l -> rows.append("""
            <tr>
              <td>%s</td>
              <td class="right">%d</td>
              <td class="right">%s</td>
              <td class="right">%s</td>
            </tr>
            """.formatted(
                htmlEscape(l.productName()),
                l.quantity(),
                fmtWon(l.unitPrice()),
                fmtWon(l.lineAmount())
        )));

        return template
                .replace("{buyerEmail}", htmlEscape(e.buyerEmail()))
                .replace("{orderId}", String.valueOf(e.orderId()))
                .replace("{paymentId}", String.valueOf(e.paymentId()))
                .replace("{paymentMethod}", htmlEscape(String.valueOf(e.paymentMethod())))
                .replace("{paidAmount}", paidAmount)
                .replace("{paidAt}", htmlEscape(paidAt))
                .replace("{address}", htmlEscape(e.address()))
                .replace("{ITEM_ROWS}", rows.toString());
    }

    private String readReceiptTemplate() {
        try (InputStream in = receiptTemplate.getInputStream()) {
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException ex) {
            throw new IllegalStateException("영수증 템플릿 로드 실패: " + receiptTemplate, ex);
        }
    }

    private String fmtWon(long won) {
        return WON_FMT.format(won) + "원";
    }

    private String formatDate(Object dt) {
        try {
            if (dt instanceof TemporalAccessor ta) return DT_FMT.format(ta);
        } catch (Exception ignore) {}
        return String.valueOf(dt);
    }

    private String htmlEscape(String s) {
        if (s == null) return "";
        return s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;");
    }
}