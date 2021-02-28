package io.opentelemetry.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${rabbitmq.flight.dg.exchange}")
	private String exchange;

	@Value("${rabbitmq.flight.received.queue}")
	private String flightQueue;
	@Value("${rabbitmq.flight.received.routingkey}")
	private String flightRoutingKey;

	@Bean
	DirectExchange directExchange() {
		return new DirectExchange(exchange);
	}

	@Bean(name = "flightReceived")
	Queue flightReceivedQueue() {
		return new Queue(flightQueue, true);
	}

	@Bean
	Binding flightReceivedBinding(@Qualifier("flightReceived") Queue queue, DirectExchange exchange) {
		return BindingBuilder.bind(queue).to(exchange).with(flightRoutingKey);
	}
}
