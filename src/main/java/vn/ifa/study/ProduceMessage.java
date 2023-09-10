package vn.ifa.study;

import java.util.UUID;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Profile("produce")
@SpringBootApplication
public class ProduceMessage {

	public static void main(final String[] args) {

		final ConfigurableApplicationContext context = SpringApplication.run(ProduceMessage.class, args);
		final Function<Customer, Customer> f = (Function<Customer, Customer>) context.getBean("createCustomer");
		f.apply(Customer.builder()
				.customerId(UUID.randomUUID()
						.toString())
				.build());

	}

	@Bean
	Function<Customer, Customer> createCustomer() {

		return this::processCustomer;
	}

	private Customer processCustomer(final Customer c) {
		log.info("Create a customer");
		return c;
	}
}
