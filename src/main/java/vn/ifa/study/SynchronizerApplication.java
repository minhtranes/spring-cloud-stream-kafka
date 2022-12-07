package vn.ifa.study;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class SynchronizerApplication {

    @Bean
    Consumer<Message<Customer>> synchronize() {

        return c -> {

            try {
                Object payload = c.getPayload();
                String traceId = (String) c.getHeaders()
                        .get(SpringCloudDemoApplication.HEADER_TRACE_ID);

                if (!(payload instanceof Customer)) {
                    return;
                }

                Customer customer = (Customer) payload;
                TimeUnit.SECONDS.sleep(1);
                log.info("Added customer {} with traceId {} into local cache", customer.getCustomerId(), traceId);
            }
            catch (Exception e) {
            }

        };
    }
}
