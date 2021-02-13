package io.opentelemetry.example.flight;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import io.opentelemetry.extension.annotations.WithSpan;

@Component
public class FlightClient {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightClient.class);

	@Value("${app.provider1.url}")
	private String provider1Url;

	@Autowired
	private RestTemplate restTemplate;

	@WithSpan
	public List<Flight> getFlights(String origin) {
		LOGGER.info("Getting Flights from {}", provider1Url);

		ResponseEntity<Flight[]> response = restTemplate.getForEntity(provider1Url, Flight[].class);

		return Arrays.asList(response.getBody());
	}
}
