package io.opentelemetry.example.flight.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.opentelemetry.example.flight.model.Flight;
import io.opentelemetry.example.flight.service.FlightService;
import io.opentelemetry.example.otel.RabbitMQTracer;

@Component
public class FlightMQConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightMQConsumer.class);

	private FlightService flightService;
	private ObjectMapper mapper;
	private RabbitMQTracer tracer;

	public FlightMQConsumer(FlightService flightService, ObjectMapper mapper, RabbitMQTracer rabbitMQTracer) {
		this.flightService = flightService;
		this.tracer = rabbitMQTracer;
		this.mapper = mapper;
	}

	@RabbitListener(queues = "#{'${rabbitmq.flight.received.queue}'}")
	public void consumeMessage(String flightMessage, Message message) {
		LOGGER.trace("Message received: {} ", flightMessage);

		MessageProperties messageProperties = message.getMessageProperties();

		tracer.doInSpan(messageProperties, () -> {
			try {
				Flight flight = create(flightMessage);
				flightService.process(flight);
				LOGGER.debug("Message processed successfully");

			} catch (Exception e) {
				LOGGER.error("Unnable to process the Message", e);
			}
		});
	}

	private Flight create(String flightMessage) throws JsonProcessingException {
		return mapper.readValue(flightMessage, Flight.class);
	}
}
