package io.opentelemetry.example.flight.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.opentelemetry.example.flight.model.Flight;

@Service
public class FlightService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);

	public void process(Flight flight) {
		LOGGER.info("Processing : {}", flight);
		
	}
}
