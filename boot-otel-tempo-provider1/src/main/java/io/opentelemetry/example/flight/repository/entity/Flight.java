package io.opentelemetry.example.flight.repository.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "flight")
public class Flight {
	
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

	private String origin;
	private String destination;
	private String airline;
	
	@Column(name = "departing")
	private Date departureTime;

	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public String getAirline() {
		return airline;
	}

	public void setAirline(String airline) {
		this.airline = airline;
	}

	public Date getDepartureTime() {
		return departureTime;
	}

	public void setDepartureTime(Date departureTime) {
		this.departureTime = departureTime;
	}

	@Override
	public String toString() {
		return "Flight [origin=" + origin + ", destination=" + destination + ", airline=" + airline + ", departureTime="
				+ departureTime + "]";
	}	
}
