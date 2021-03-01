package io.opentelemetry.example.otel;

import org.springframework.amqp.core.MessageProperties;
import org.springframework.stereotype.Component;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.Scope;
import io.opentelemetry.semconv.trace.attributes.SemanticAttributes;

@Component
public class RabbitMQTracer {

	private Tracer tracer = GlobalOpenTelemetry.getTracer("io.opentelemetry.javaagent.rabbitmq");

	public void doInSpan(MessageProperties messageProperties, SpanCallback callback) {

		Context extractedContext = extractContext(messageProperties);

		Span serverSpan = null;
		try (Scope scope = extractedContext.makeCurrent()) {
			serverSpan = buildSpan(messageProperties);
			try {
				callback.doInSpan();
			} catch (Exception e) {				
				serverSpan.recordException(e);
				serverSpan.setStatus(StatusCode.ERROR);
				throw new RuntimeException(e);
			} finally {
				serverSpan.end();
			}
		}		
	}

	private Context extractContext(MessageProperties messageProperties) {
		return GlobalOpenTelemetry.getPropagators().getTextMapPropagator()
				.extract(Context.current(), messageProperties.getHeaders(), new RabbitTextMapExtractAdapter());
	}

	private Span buildSpan(MessageProperties messageProperties) {

		// Automatically use the extracted SpanContext as parent.
		Span serverSpan = tracer.spanBuilder(spanNameOnGet(messageProperties.getConsumerQueue()))
				.setSpanKind(Span.Kind.CONSUMER)
				.setAttribute(SemanticAttributes.MESSAGING_SYSTEM, "rabbitmq")
				.setAttribute(SemanticAttributes.MESSAGING_DESTINATION_KIND, "queue")
				.setAttribute(SemanticAttributes.MESSAGING_DESTINATION,
						messageProperties.getReceivedExchange())
				.setAttribute("rabbitmq.routing_key", messageProperties.getReceivedRoutingKey())
				.setAttribute(SemanticAttributes.MESSAGING_MESSAGE_PAYLOAD_SIZE_BYTES,
						messageProperties.getContentLength())
				.setAttribute(SemanticAttributes.MESSAGING_OPERATION, "receive").startSpan();
		return serverSpan;
	}

	private String spanNameOnGet(String queue) {
		return (queue.startsWith("amq.gen-") ? "<generated>" : queue) + " receive";
	}
}
