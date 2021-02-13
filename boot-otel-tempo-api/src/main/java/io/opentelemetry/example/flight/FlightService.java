package io.opentelemetry.example.flight;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FlightService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlightService.class);
	
	private FlightClient flightClient;
	
	public FlightService(FlightClient flightClient) {
		this.flightClient = flightClient;
	}

	public List<Flight> getFlights(String origin) {
		LOGGER.info("Getting flights for {}", origin);
		return this.flightClient.getFlights(origin);
	}
}
