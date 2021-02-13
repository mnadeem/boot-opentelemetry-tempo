package io.opentelemetry.example.flight;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;

public class FlightService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

	public List<Flight> getFlights(String origin) {
		// TODO Auto-generated method stub
		return null;
	}

	@WithSpan
    public void doSomeWorkNewSpan() {
		LOGGER.info("Doing some work In New span");
        Span span = Span.current();
 
        span.setAttribute("template.a2", "some value");
 
        span.addEvent("template.processing2.start", atttributes("321"));
        span.addEvent("template.processing2.end", atttributes("321"));
    }
 
    private Attributes atttributes(String id) {
        return Attributes.of(AttributeKey.stringKey("app.id"), id);
    }
}
