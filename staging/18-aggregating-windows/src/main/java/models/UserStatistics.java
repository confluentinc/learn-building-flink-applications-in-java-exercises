package models;

import java.time.Duration;
import java.util.Objects;

public class UserStatistics {
    private String emailAddress;
    private Duration totalFlightDuration;
    private long numberOfFlights;

    public UserStatistics() {
    }

    public UserStatistics(FlightData flightData) {
        this.emailAddress = flightData.getEmailAddress();
        this.totalFlightDuration = Duration.between(
                flightData.getDepartureTime(),
                flightData.getArrivalTime()
        );
        this.numberOfFlights = 1;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Duration getTotalFlightDuration() {
        return totalFlightDuration;
    }

    public void setTotalFlightDuration(Duration totalFlightDuration) {
        this.totalFlightDuration = totalFlightDuration;
    }

    public long getNumberOfFlights() {
        return numberOfFlights;
    }

    public void setNumberOfFlights(long numberOfFlights) {
        this.numberOfFlights = numberOfFlights;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStatistics that = (UserStatistics) o;
        return numberOfFlights == that.numberOfFlights && Objects.equals(emailAddress, that.emailAddress) && Objects.equals(totalFlightDuration, that.totalFlightDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(emailAddress, totalFlightDuration, numberOfFlights);
    }

    @Override
    public String toString() {
        return "UserStatistics{" +
                "emailAddress='" + emailAddress + '\'' +
                ", totalFlightDuration=" + totalFlightDuration +
                ", numberOfFlights=" + numberOfFlights +
                '}';
    }
}