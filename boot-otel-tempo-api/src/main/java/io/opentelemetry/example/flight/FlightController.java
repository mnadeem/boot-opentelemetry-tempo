package io.opentelemetry.example.flight;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FlightController {
	
	 private static final Logger LOGGER = LoggerFactory.getLogger(FlightController.class);
	
	private FlightService flightService;
	
	public FlightController(FlightService flightService) {
		this.flightService = flightService;
	}
	
	@GetMapping("/flights")
    public List<Flight> greeting(@RequestParam(value = "origin", defaultValue = "India") String origin) {
    	LOGGER.info("Before Service Method Call");
        return flightService.getFlights(origin);
    }

}
