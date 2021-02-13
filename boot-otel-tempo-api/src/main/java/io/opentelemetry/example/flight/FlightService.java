package io.opentelemetry.example.flight;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.extension.annotations.WithSpan;

@Service
public class FlightService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);
	
	private FlightClient flightClient;
	
	public FlightService(FlightClient flightClient) {
		this.flightClient = flightClient;
	}

	public List<Flight> getFlights(String origin) {
		LOGGER.info("Getting flights for {}", origin);
		List<Flight> flights = this.flightClient.getFlights(origin);
		doSomeWorkNewSpan();
		return flights;
	}
	
	@WithSpan
    private void doSomeWorkNewSpan() {
		LOGGER.info("Doing some work In New span");
        Span span = Span.current();
 
        span.setAttribute("attribute.a2", "some value");
 
        span.addEvent("app.processing2.start", atttributes("321"));
        span.addEvent("app.processing2.end", atttributes("321"));
    }
 
    private Attributes atttributes(String id) {
        return Attributes.of(AttributeKey.stringKey("app.id"), id);
    }
}
