package io.opentelemetry.example.flight.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.opentelemetry.example.flight.model.Flight;
import io.opentelemetry.example.flight.service.FlightService;
import io.opentelemetry.extension.annotations.WithSpan;

@Component
public class FlightMQConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightMQConsumer.class);

	@Autowired
	private FlightService flightService;

	@Autowired
	private ObjectMapper mapper;

	@RabbitListener(queues = "#{'${rabbitmq.flight.received.queue}'}")
	@WithSpan
	public void consumeMessage(String flightMessage) {
		try {
			LOGGER.trace("Message received: {} ", flightMessage);
			Flight flight = create(flightMessage);
			flightService.process(flight);
			LOGGER.debug("Message processed successfully");
		} catch (Exception e) {
			LOGGER.error("Unnable to process the Message", e);
		}
	}

	private Flight create(String flightMessage) throws JsonProcessingException {
		return mapper.readValue(flightMessage, Flight.class);
	}
}
