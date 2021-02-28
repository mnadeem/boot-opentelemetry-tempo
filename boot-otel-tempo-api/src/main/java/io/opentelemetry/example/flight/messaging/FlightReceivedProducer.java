package io.opentelemetry.example.flight.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.opentelemetry.example.flight.Flight;

@Component
public class FlightReceivedProducer {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightReceivedProducer.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Value("${rabbitmq.flight.dg.exchange}")
	private String exchangeName;

	@Value("${rabbitmq.flight.received.routingkey}")
	private String routingKey;
	
	@Autowired
	private ObjectMapper objectMapper;

	public void sendMessage(Flight flight) {
		try {
			String jsonMessage = objectMapper.writeValueAsString(flight);
			rabbitTemplate.convertAndSend(exchangeName, routingKey, jsonMessage);
			LOGGER.debug("Sent message for flight");
		} catch (Exception e) {
			LOGGER.error("Unable to send Message ", e);
		}
	}
}