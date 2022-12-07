package vn.ifa.study;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

@Configuration
public class ProducerApplication {

    public static final String HEADER_TRACE_ID = "traceId";

    @Bean
    Supplier<Message<Customer>> produceCustomer() {

        return () -> MessageBuilder.withPayload(Customer.builder()
                .customerId(UUID.randomUUID()
                        .toString())
                .build())
                .setHeader(HEADER_TRACE_ID,
                           UUID.randomUUID()
                                   .toString())
                .build();
    }
}
