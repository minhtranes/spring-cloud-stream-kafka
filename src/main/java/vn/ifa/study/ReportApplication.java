package vn.ifa.study;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ReportApplication {
    private final Map<String, Customer> store = new HashMap<>();

    private final AtomicInteger counter = new AtomicInteger();

    private void processCustomer(final Message<Customer> c) {

        Object payload = c.getPayload();
        String traceId = (String) c.getHeaders()
                .get(ProcessorApplication.HEADER_TRACE_ID);

        if (!(payload instanceof Customer)) {
            store.remove(traceId);
            log.info("ACTIVE COUNT: {}   DELETED COUNT: {}", store.size(), counter.incrementAndGet());
            return;
        }

        Customer customer = (Customer) payload;
        store.put(traceId, customer);
    }

    @Bean
    Consumer<Message<Customer>> report() {

        return this::processCustomer;
    }

}
