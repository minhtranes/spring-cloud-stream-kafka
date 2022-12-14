package vn.ifa.study;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class ReportApplication {
    private final Map<String, Message<?>> store = new ConcurrentHashMap<>();

    private final AtomicInteger counter = new AtomicInteger();
    private final AtomicInteger tombstomeCounter = new AtomicInteger();
    private final AtomicInteger duplicatedCounter = new AtomicInteger();

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    KafkaNullConverter converter;

    private void accumulate(final Message<?> c) {

        Object payload = c.getPayload();
        String key = (String) c.getHeaders()
                .get(KafkaHeaders.RECEIVED_MESSAGE_KEY);
        long offset = (long) c.getHeaders()
                .get(KafkaHeaders.OFFSET);

        if (isTombstome(c)) {
            store.remove(key);
            tombstomeCounter.incrementAndGet();
        } else {
            Message<?> previous = store.put(key, c);

            if (previous != null) {

                if (log.isDebugEnabled()) {
                    JsonNode prevJson = mapper.convertValue(previous.getPayload(), JsonNode.class);
                    JsonNode currJson = mapper.convertValue(payload, JsonNode.class);
                    log.debug("PREV: {}", prevJson.toString());
                    log.debug("CURR: {}", currJson.toString());
                }

                duplicatedCounter.incrementAndGet();
                long prevOffset = (long) previous.getHeaders()
                        .get(KafkaHeaders.OFFSET);
                log.info("Duplicated with one at offset {}", prevOffset);

            }

        }

        log.info("KEY: {}   OFFSET: {}  MESSAGE: {}   COMPACTED SIZE: {}   TOMBSTONE: {}   DUPLICATED: {}",
                 key,
                 offset,
                 counter.incrementAndGet(),
                 store.size(),
                 tombstomeCounter.get(),
                 duplicatedCounter.get());
    }

    private boolean isTombstome(final Message<?> o) {

        return (o.getPayload() == null) || converter.canConvertFrom(o, KafkaNull.class);
    }

    @Bean
    Consumer<Message<?>> report() {

        return this::accumulate;
    }

}
