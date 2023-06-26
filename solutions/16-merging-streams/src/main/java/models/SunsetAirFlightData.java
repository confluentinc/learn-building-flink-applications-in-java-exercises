package models;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SunsetAirFlightData {
    private String customerEmailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime departureTime;
    private String departureAirport;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime arrivalTime;
    private String arrivalAirport;
    private String flightId;
    private String referenceNumber;

    @JsonCreator
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SunsetAirFlightData that = (SunsetAirFlightData) o;
        return Objects.equals(customerEmailAddress, that.customerEmailAddress) && Objects.equals(departureTime, that.departureTime) && Objects.equals(departureAirport, that.departureAirport) && Objects.equals(arrivalTime, that.arrivalTime) && Objects.equals(arrivalAirport, that.arrivalAirport) && Objects.equals(flightId, that.flightId) && Objects.equals(referenceNumber, that.referenceNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerEmailAddress, departureTime, departureAirport, arrivalTime, arrivalAirport, flightId, referenceNumber);
    }

    @Override
    public String toString() {
        return "SkyOneAirlinesFlightData{" +
                "customerEmailAddress='" + customerEmailAddress + '\'' +
                ", departureTime=" + departureTime +
                ", departureAirport='" + departureAirport + '\'' +
                ", arrivalTime=" + arrivalTime +
                ", arrivalAirport='" + arrivalAirport + '\'' +
                ", flightId='" + flightId + '\'' +
                ", referenceNumber='" + referenceNumber + '\'' +
                '}';
    }

    public FlightData toFlightData() {
        FlightData flightData = new FlightData();

        flightData.setEmailAddress(getCustomerEmailAddress());
        flightData.setDepartureTime(getDepartureTime());
        flightData.setDepartureAirportCode(getDepartureAirport());
        flightData.setArrivalTime(getArrivalTime());
        flightData.setArrivalAirportCode(getArrivalAirport());
        flightData.setFlightNumber(getFlightId());
        flightData.setConfirmationCode(getReferenceNumber());

        return flightData;
    }
}
