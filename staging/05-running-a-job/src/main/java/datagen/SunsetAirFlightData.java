package datagen;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.Objects;
import java.util.UUID;

public class SunsetAirFlightData {
    private String customerEmailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime departureTime;
    private String departureAirport;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime arrivalTime;
    private String arrivalAirport;
    private Duration flightDuration;
    private String flightId;
    private String referenceNumber;
    private BigDecimal totalPrice;
    private String aircraftDetails;

    public SunsetAirFlightData() {
    }

    public String getCustomerEmailAddress() {
        return customerEmailAddress;
    }

    public void setCustomerEmailAddress(String customerEmailAddress) {
        this.customerEmailAddress = customerEmailAddress;
    }

    public ZonedDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(ZonedDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public ZonedDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(ZonedDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public Duration getFlightDuration() {
        return flightDuration;
    }

    public void setFlightDuration(Duration flightDuration) {
        this.flightDuration = flightDuration;
    }

    public String getFlightId() {
        return flightId;
    }

    public void setFlightId(String flightId) {
        this.flightId = flightId;
    }

    public String getReferenceNumber() {
        return referenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getAircraftDetails() {
        return aircraftDetails;
    }

    public void setAircraftDetails(String aircraftDetails) {
        this.aircraftDetails = aircraftDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SunsetAirFlightData that = (SunsetAirFlightData) o;
        return Objects.equals(customerEmailAddress, that.customerEmailAddress) && Objects.equals(departureTime, that.departureTime) && Objects.equals(departureAirport, that.departureAirport) && Objects.equals(arrivalTime, that.arrivalTime) && Objects.equals(arrivalAirport, that.arrivalAirport) && Objects.equals(flightDuration, that.flightDuration) && Objects.equals(flightId, that.flightId) && Objects.equals(referenceNumber, that.referenceNumber) && Objects.equals(totalPrice, that.totalPrice) && Objects.equals(aircraftDetails, that.aircraftDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerEmailAddress, departureTime, departureAirport, arrivalTime, arrivalAirport, flightDuration, flightId, referenceNumber, totalPrice, aircraftDetails);
    }

    @Override
    public String toString() {
        return "SunsetAirFlightData{" +
            "customerEmailAddress='" + customerEmailAddress + '\'' +
            ", departureTime=" + departureTime +
            ", departureAirport='" + departureAirport + '\'' +
            ", arrivalTime=" + arrivalTime +
            ", arrivalAirport='" + arrivalAirport + '\'' +
            ", flightDuration=" + flightDuration +
            ", flightId='" + flightId + '\'' +
            ", referenceNumber='" + referenceNumber + '\'' +
            ", totalPrice=" + totalPrice +
            ", aircraftDetails='" + aircraftDetails + '\'' +
            '}';
    }
}
