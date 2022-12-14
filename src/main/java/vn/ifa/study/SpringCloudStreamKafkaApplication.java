package vn.ifa.study;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SpringCloudStreamKafkaApplication {
    public static void main(final String[] args) {

        SpringApplication.run(SpringCloudStreamKafkaApplication.class, args);
    }

    @Bean
    KafkaNullConverter converter() {

        return new KafkaNullConverter();
    }

}
