package vn.ifa.study;

import java.util.Collections;

import org.springframework.kafka.support.KafkaNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.converter.AbstractMessageConverter;
import org.springframework.util.MimeTypeUtils;

public class KafkaNullConverter extends AbstractMessageConverter {

    public KafkaNullConverter() {

        super(Collections.singletonList(MimeTypeUtils.ALL));
    }

    @Override
    protected boolean canConvertFrom(final Message<?> message, final Class<?> targetClass) {

        return message.getPayload() instanceof KafkaNull;
    }

    @Override
    protected Object convertFromInternal(
        final Message<?> message,
        final Class<?> targetClass,
        final Object conversionHint) {

        return message.getPayload();
    }

    @Override
    protected Object convertToInternal(
        final Object payload,
        final MessageHeaders headers,
        final Object conversionHint) {

        return payload;
    }

    @Override
    protected boolean supports(final Class<?> aClass) {

        return KafkaNull.class.equals(aClass);
    }

    @Override
    protected boolean supportsMimeType(final MessageHeaders headers) {

        return true;
    }

}
