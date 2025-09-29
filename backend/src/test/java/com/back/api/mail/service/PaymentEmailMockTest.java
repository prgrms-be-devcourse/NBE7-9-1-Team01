package com.back.api.mail.service;

import com.back.api.payment.controller.PaymentController;
import com.back.api.payment.dto.request.PaymentCreateRequest;
import com.back.domain.member.entity.Member;
import com.back.domain.member.entity.Role;
import com.back.domain.member.repository.MemberRepository;
import com.back.domain.order.entity.Order;
import com.back.domain.order.entity.OrderProduct;
import com.back.domain.order.entity.OrderStatus;
import com.back.domain.order.repository.OrderProductRepository;
import com.back.domain.order.repository.OrderRepository;
import com.back.domain.payment.entity.PaymentMethod;
import com.back.domain.product.entity.Product;
import com.back.domain.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.Address;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeMessage;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PaymentEmailMockTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @Autowired private MemberRepository memberRepository;
    @Autowired private ProductRepository productRepository;
    @Autowired private OrderRepository orderRepository;
    @Autowired private OrderProductRepository orderProductRepository;

    @MockitoBean
    private JavaMailSender javaMailSender;

    private Long orderId;

    @BeforeAll
    void setUpData() {
        Member member = new Member("test@naver.com", null, "경기도 부천", "55555", Role.ROLE_USER);
        memberRepository.save(member);

        Product product1 = productRepository.findById(1L).orElse(null);
        Product product2 = productRepository.findById(2L).orElse(null);

        if (product1 == null) {
            product1 = productRepository.save(new Product("Columbia Narino", "aaaa", 5000L, "커피콩"));
        }
        if (product2 == null) {
            product2 = productRepository.save(new Product("Brazil Serra Do Caparao", "aaaa", 7000L, "커피콩"));
        }

        Order order = new Order(member, OrderStatus.PENDING, LocalDateTime.now());
        orderRepository.save(order);
        orderId = order.getId();

        orderProductRepository.save(new OrderProduct(order, product1, 2L));
        orderProductRepository.save(new OrderProduct(order, product2, 3L));
    }


    @Test
    @DisplayName("메일 전송 예외가 발생해도 결제 API는 성공(트랜잭션 격리)")
    void email_failure_does_not_break_payment() throws Exception {
        // given
        doThrow(new MailSendException("SMTP unavailable"))
                .when(javaMailSender).send(any(SimpleMailMessage.class));

        PaymentCreateRequest request = new PaymentCreateRequest(orderId, PaymentMethod.CREDIT_CARD);

        // when & then:
        mockMvc.perform(
                        post("/api/payments")
                                .contentType(APPLICATION_JSON)
                                .accept(APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("createPayment"))
                .andExpect(status().isOk());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED) // 이 테스트만 Tx 없이 → AFTER_COMMIT 리스너 실행
    @DisplayName("결제 생성 시 메일 전송 호출되고 본문에 핵심 값들이 포함된다")
    void email_sent_with_expected_body() throws Exception {
        // given
        PaymentCreateRequest request = new PaymentCreateRequest(orderId, PaymentMethod.CREDIT_CARD);

        // when
        ResultActions result = mockMvc.perform(
                post("/api/payments")
                        .contentType(APPLICATION_JSON)
                        .accept(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        );

        // then A
        result.andExpect(handler().handlerType(PaymentController.class))
                .andExpect(handler().methodName("createPayment"))
                .andExpect(status().isOk());

        String body = Awaitility.await().atMost(Duration.ofSeconds(2)).until(() -> {
            try {
                ArgumentCaptor<SimpleMailMessage> simpleCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
                verify(javaMailSender).send(simpleCaptor.capture());
                SimpleMailMessage msg = simpleCaptor.getValue();

                assertThat(msg.getTo()).contains("test@naver.com");
                assertThat(msg.getSubject()).contains("결제 완료");
                return msg.getText(); // 본문
            } catch (AssertionError notSimple) {
                try {
                    ArgumentCaptor<MimeMessagePreparator> prepCaptor = ArgumentCaptor.forClass(MimeMessagePreparator.class);
                    verify(javaMailSender).send(prepCaptor.capture());

                    MimeMessagePreparator prep = prepCaptor.getValue();

                    JavaMailSenderImpl temp = new JavaMailSenderImpl();
                    MimeMessage mime = temp.createMimeMessage();
                    prep.prepare(mime);

                    Address[] to = mime.getRecipients(Message.RecipientType.TO);
                    assertThat(to).isNotNull();
                    assertThat(Arrays.stream(to).map(Address::toString).toList())
                            .anyMatch(s -> s.contains("test@naver.com"));
                    assertThat(mime.getSubject()).contains("결제 완료");

                    return extractText(mime);
                } catch (AssertionError notPreparator) {
                    return null;
                }
            }
        }, Objects::nonNull);

        assertThat(body).contains("결제수단 : 신용카드");
        assertThat(body).contains("결제금액 : 31,000원");
        assertThat(body).contains("주소     : 경기도 부천");
        assertThat(body).contains("Columbia Narino");
        assertThat(body).contains("Brazil Serra Do Caparao");
    }

    private static String extractText(MimeMessage mime) throws Exception {
        Object content = mime.getContent();
        if (content instanceof String s) return s;

        if (content instanceof Multipart mp) {
            for (int i = 0; i < mp.getCount(); i++) {
                var part = mp.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) continue;
                String ct = part.getContentType();
                if (ct != null && (ct.startsWith("text/plain") || ct.startsWith("text/html"))) {
                    Object v = part.getContent();
                    return (v instanceof String) ? (String) v : String.valueOf(v);
                }
            }
        }
        return "";
    }


}