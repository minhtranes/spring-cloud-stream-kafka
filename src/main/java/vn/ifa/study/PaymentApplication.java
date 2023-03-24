package vn.ifa.study;

import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class PaymentApplication {

    private void accumulate(final Message<?> msg) {

        log.info("Sample to consume message");

    }

    @Bean
    Consumer<Message<?>> payment() {

        return this::accumulate;
    }

}
