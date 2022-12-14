package vn.ifa.study;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ProcessorApplication {

    public static final String HEADER_TRACE_ID = "traceId";
    public static final String HEADER_KEY = "key";
    public static final String HEADER_OFFSET = "key";

    @Bean
    Function<Message<Customer>, Message<?>> processCustomer() {

        return this::processCustomer;
    }

    private Message<KafkaNull> processCustomer(final Message<Customer> c) {

        Object payload = c.getPayload();

        if (!(payload instanceof Customer)) {
            log.warn("Receive not a customer message!!");
            return null;
        }

        try {

            Customer customer = (Customer) payload;
            Object traceId = c.getHeaders()
                    .get(HEADER_TRACE_ID);
            final String customerId = customer.getCustomerId();
            log.info("Start process customer {} and traceId {}", customerId, traceId);
            TimeUnit.SECONDS.sleep(10);
            log.info("Processing customer: {} and traceId {}", customerId, traceId);

        }
        catch (final InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return MessageBuilder.withPayload(KafkaNull.INSTANCE)
                .setHeader(HEADER_TRACE_ID,
                           c.getHeaders()
                                   .get(HEADER_TRACE_ID))
                .build();
    }

}
