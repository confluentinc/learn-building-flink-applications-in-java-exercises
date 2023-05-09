package models;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonCreator;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.ZonedDateTime;
import java.util.Objects;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SkyOneAirlinesFlightData {
    private String emailAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime flightDepartureTime;
    private String iataDepartureCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private ZonedDateTime flightArrivalTime;
    private String iataArrivalCode;
    private String flightNumber;
    private String confirmation;

    @JsonCreator
    public SkyOneAirlinesFlightData() {
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public ZonedDateTime getFlightDepartureTime() {
        return flightDepartureTime;
    }

    public void setFlightDepartureTime(ZonedDateTime flightDepartureTime) {
        this.flightDepartureTime = flightDepartureTime;
    }

    public String getIataDepartureCode() {
        return iataDepartureCode;
    }

    public void setIataDepartureCode(String iataDepartureCode) {
        this.iataDepartureCode = iataDepartureCode;
    }

    public ZonedDateTime getFlightArrivalTime() {
        return flightArrivalTime;
    }

    public void setFlightArrivalTime(ZonedDateTime flightArrivalTime) {
        this.flightArrivalTime = flightArrivalTime;
    }

    public String getIataArrivalCode() {
        return iataArrivalCode;
    }

    public void setIataArrivalCode(String iataArrivalCode) {
        this.iataArrivalCode = iataArrivalCode;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SkyOneAirlinesFlightData that = (SkyOneAirlinesFlightData) o;
        return Objects.equals(emailAddress, that.emailAddress) && Objects.equals(flightDepartureTime, that.flightDepartureTime) && Objects.equals(iataDepartureCode, that.iataDepartureCode) && Objects.equals(flightArrivalTime, that.flightArrivalTime) && Objects.equals(iataArrivalCode, that.iataArrivalCode) && Objects.equals(flightNumber, that.flightNumber) && Objects.equals(confirmation, that.confirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, flightDepartureTime, iataDepartureCode, flightArrivalTime, iataArrivalCode, flightNumber, confirmation);
    }

    @Override
    public String toString() {
        return "SkyOneAirlinesFlightData{" +
                "emailAddress='" + emailAddress + '\'' +
                ", flightDepartureTime=" + flightDepartureTime +
                ", iataDepartureCode='" + iataDepartureCode + '\'' +
                ", flightArrivalTime=" + flightArrivalTime +
                ", iataArrivalCode='" + iataArrivalCode + '\'' +
                ", flightNumber='" + flightNumber + '\'' +
                ", confirmation='" + confirmation + '\'' +
                '}';
    }

    public FlightData toFlightData() {
        FlightData flightData = new FlightData();

        flightData.setEmailAddress(getEmailAddress());
        flightData.setDepartureTime(getFlightDepartureTime());
        flightData.setDepartureAirportCode(getIataDepartureCode());
        flightData.setArrivalTime(getFlightArrivalTime());
        flightData.setArrivalAirportCode(getIataArrivalCode());
        flightData.setFlightNumber(getFlightNumber());
        flightData.setConfirmationCode(getConfirmation());

        return flightData;
    }
}
