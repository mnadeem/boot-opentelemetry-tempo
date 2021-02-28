package io.opentelemetry.example.flight.messaging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.example.flight.messaging.otel.RabbitTextMapExtractAdapter;
import io.opentelemetry.example.flight.model.Flight;
import io.opentelemetry.example.flight.service.FlightService;

@Component
public class FlightMQConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightMQConsumer.class);

	@Autowired
	private FlightService flightService;

	@Autowired
	private ObjectMapper mapper;

	@RabbitListener(queues = "#{'${rabbitmq.flight.received.queue}'}")
	public void consumeMessage(String flightMessage, Message message) {
		MessageProperties messageProperties = message.getMessageProperties();

		Context extractedContext = GlobalOpenTelemetry.getPropagators().getTextMapPropagator()
				.extract(Context.current(), messageProperties.getHeaders(), new RabbitTextMapExtractAdapter());

		Tracer tracer = GlobalOpenTelemetry.getTracer("io.opentelemetry.javaagent.rabbitmq");

		Span serverSpan = null;
		try (Scope scope = extractedContext.makeCurrent()) {
			// Automatically use the extracted SpanContext as parent.
			serverSpan = tracer.spanBuilder(spanNameOnGet(messageProperties.getConsumerQueue()))
					.setSpanKind(Span.Kind.CONSUMER)
					.setAttribute(SemanticAttributes.MESSAGING_SYSTEM, "rabbitmq")
					.setAttribute(SemanticAttributes.MESSAGING_DESTINATION_KIND, "queue")
					.setAttribute(SemanticAttributes.MESSAGING_DESTINATION,
							messageProperties.getReceivedExchange())
					.setAttribute("rabbitmq.routing_key", messageProperties.getReceivedRoutingKey())
					.setAttribute(SemanticAttributes.MESSAGING_MESSAGE_PAYLOAD_SIZE_BYTES,
							messageProperties.getContentLength())
					.setAttribute(SemanticAttributes.MESSAGING_OPERATION, "receive").startSpan();
			try {

				LOGGER.trace("Message received: {} ", flightMessage);
				Flight flight = create(flightMessage);
				flightService.process(flight);
				LOGGER.debug("Message processed successfully");

			} catch (Exception e) {
				LOGGER.error("Unnable to process the Message", e);
				serverSpan.recordException(e);
				serverSpan.setStatus(StatusCode.ERROR);
			} finally {
				serverSpan.end();
			}
		}
	}

	public String spanNameOnGet(String queue) {
		return (queue.startsWith("amq.gen-") ? "<generated>" : queue) + " receive";
	}

	private Flight create(String flightMessage) throws JsonProcessingException {
		return mapper.readValue(flightMessage, Flight.class);
	}
}
