package models;

import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

public class TestHelpers {
    private static Random random = new Random(System.currentTimeMillis());

    public static String generateAirportCode() {
        String[] airports = new String[] {
                "ATL", "DFW", "DEN", "ORD", "LAX", "CLT", "MCO", "LAS", "PHX", "MIA",
                "SEA", "IAH", "JFK", "EWR", "FLL", "MSP", "SFO", "DTW", "BOS", "SLC",
                "PHL", "BWI", "TPA", "SAN", "LGA", "MDW", "BNA", "IAD", "DCA", "AUS"
        };

        return airports[random.nextInt(airports.length)];
    }

    public static String generateString(int size) {
        final String alphaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder(size);

        for(int i = 0; i < size; i++) {
            final int index = random.nextInt(alphaString.length());
            sb.append(alphaString.charAt(index));
        }

        return sb.toString();
    }

    public static String generateEmail() {
        return generateString(10)+"@email.com";
    }

    public static ZonedDateTime generateDepartureTime() {
        return LocalDate.now()
                .plusDays(random.nextInt(365))
                .atTime(random.nextInt(24), random.nextInt(60))
                .atZone(ZoneId.of("UTC"));
    }

    public static ZonedDateTime generateArrivalTime(ZonedDateTime departure) {
        return departure
                .plusHours(random.nextInt(15))
                .plusMinutes(random.nextInt(60));
    }

    public static Duration generateDuration() {
        return Duration.ofMinutes(random.nextInt(300));
    }

    public static class SkyOneBuilder {
        private String emailAddress;
        private ZonedDateTime flightDepartureTime;
        private String iataDepartureCode;
        private ZonedDateTime flightArrivalTime;
        private String iataArrivalCode;
        private String flightNumber;
        private String confirmation;

        public SkyOneBuilder() {
            this.emailAddress = generateEmail();
            this.flightDepartureTime = generateDepartureTime();
            this.iataDepartureCode = generateAirportCode();
            this.flightArrivalTime = generateArrivalTime(flightDepartureTime);
            this.iataArrivalCode = generateAirportCode();
            this.flightNumber = "SKY1"+random.nextInt(1000);
            this.confirmation = "SKY1"+generateString(6);
        }

        public SkyOneBuilder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public SkyOneBuilder setFlightDepartureTime(ZonedDateTime flightDepartureTime) {
            this.flightDepartureTime = flightDepartureTime;
            return this;
        }

        public SkyOneBuilder setIataDepartureCode(String iataDepartureCode) {
            this.iataDepartureCode = iataDepartureCode;
            return this;
        }

        public SkyOneBuilder setFlightArrivalTime(ZonedDateTime flightArrivalTime) {
            this.flightArrivalTime = flightArrivalTime;
            return this;
        }

        public SkyOneBuilder setIataArrivalCode(String iataArrivalCode) {
            this.iataArrivalCode = iataArrivalCode;
            return this;
        }

        public SkyOneBuilder setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
            return this;
        }

        public SkyOneBuilder setConfirmation(String confirmation) {
            this.confirmation = confirmation;
            return this;
        }

        public SkyOneAirlinesFlightData build() {
            SkyOneAirlinesFlightData skyOne = new SkyOneAirlinesFlightData();

            skyOne.setEmailAddress(emailAddress);
            skyOne.setFlightDepartureTime(flightDepartureTime);
            skyOne.setIataDepartureCode(iataDepartureCode);
            skyOne.setFlightArrivalTime(flightArrivalTime);
            skyOne.setIataArrivalCode(iataArrivalCode);
            skyOne.setFlightNumber(flightNumber);
            skyOne.setConfirmation(confirmation);

            return skyOne;
        }
    }

    public static class SunsetBuilder {
        private String customerEmailAddress = generateEmail();
        private ZonedDateTime departureTime = generateDepartureTime();
        private String departureAirport = generateAirportCode();
        private ZonedDateTime arrivalTime = generateArrivalTime(departureTime);
        private String arrivalAirport = generateAirportCode();
        private String flightId = "SUN"+random.nextInt(1000);
        private String referenceNumber = "SUN"+generateString(8);

        public SunsetBuilder() {
            this.customerEmailAddress = generateEmail();
            this.departureTime = generateDepartureTime();
            this.departureAirport = generateAirportCode();
            this.arrivalTime = generateArrivalTime(departureTime);
            this.arrivalAirport = generateAirportCode();
            this.flightId = "SUN"+random.nextInt(1000);
            this.referenceNumber = "SUN"+generateString(8);
        }

        public SunsetBuilder setCustomerEmailAddress(String customerEmailAddress) {
            this.customerEmailAddress = customerEmailAddress;
            return this;
        }

        public SunsetBuilder setDepartureTime(ZonedDateTime departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public SunsetBuilder setDepartureAirport(String departureAirport) {
            this.departureAirport = departureAirport;
            return this;
        }

        public SunsetBuilder setArrivalTime(ZonedDateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public SunsetBuilder setArrivalAirport(String arrivalAirport) {
            this.arrivalAirport = arrivalAirport;
            return this;
        }

        public SunsetBuilder setFlightId(String flightId) {
            this.flightId = flightId;
            return this;
        }

        public SunsetBuilder setReferenceNumber(String referenceNumber) {
            this.referenceNumber = referenceNumber;
            return this;
        }

        public SunsetAirFlightData build() {
            SunsetAirFlightData sunset = new SunsetAirFlightData();

            sunset.setCustomerEmailAddress(customerEmailAddress);
            sunset.setDepartureTime(departureTime);
            sunset.setDepartureAirport(departureAirport);
            sunset.setArrivalTime(arrivalTime);
            sunset.setArrivalAirport(arrivalAirport);
            sunset.setFlightId(flightId);
            sunset.setReferenceNumber(referenceNumber);

            return sunset;
        }
    }


    public static class FlightDataBuilder {
        private String emailAddress;
        private ZonedDateTime departureTime;
        private String departureAirportCode;
        private ZonedDateTime arrivalTime;
        private String arrivalAirportCode;
        private String flightNumber;
        private String confirmationCode;

        public FlightDataBuilder() {
            emailAddress = generateEmail();
            departureTime = generateDepartureTime();
            departureAirportCode = generateAirportCode();
            arrivalTime = generateArrivalTime(departureTime);
            arrivalAirportCode = generateAirportCode();
            flightNumber = "Flight"+random.nextInt(1000);
            confirmationCode = "Confirmation"+generateString(5);
        }

        public FlightDataBuilder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public FlightDataBuilder setDepartureTime(ZonedDateTime departureTime) {
            this.departureTime = departureTime;
            return this;
        }

        public FlightDataBuilder setDepartureAirportCode(String departureAirportCode) {
            this.departureAirportCode = departureAirportCode;
            return this;
        }

        public FlightDataBuilder setArrivalTime(ZonedDateTime arrivalTime) {
            this.arrivalTime = arrivalTime;
            return this;
        }

        public FlightDataBuilder setArrivalAirportCode(String arrivalAirportCode) {
            this.arrivalAirportCode = arrivalAirportCode;
            return this;
        }

        public FlightDataBuilder setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
            return this;
        }

        public FlightDataBuilder setConfirmationCode(String confirmationCode) {
            this.confirmationCode = confirmationCode;
            return this;
        }

        public FlightData build() {
            FlightData flightData = new FlightData();

            flightData.setEmailAddress(this.emailAddress);
            flightData.setDepartureTime(this.departureTime);
            flightData.setDepartureAirportCode(this.departureAirportCode);
            flightData.setArrivalTime(this.arrivalTime);
            flightData.setArrivalAirportCode(this.arrivalAirportCode);
            flightData.setFlightNumber(this.flightNumber);
            flightData.setConfirmationCode(this.confirmationCode);

            return flightData;
        }
    }

    public static class UserStatisticsBuilder {
        private String emailAddress;
        private Duration totalFlightDuration;
        private long numberOfFlights;

        public UserStatisticsBuilder() {
            this.emailAddress = generateEmail();
            ZonedDateTime departure = generateDepartureTime();
            ZonedDateTime arrival = generateArrivalTime(departure);
            this.totalFlightDuration = Duration.between(departure, arrival);
            this.numberOfFlights = random.nextInt(5);
        }

        public UserStatisticsBuilder setEmailAddress(String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        public UserStatisticsBuilder setTotalFlightDuration(Duration totalFlightDuration) {
            this.totalFlightDuration = totalFlightDuration;
            return this;
        }

        public UserStatisticsBuilder setNumberOfFlights(long numberOfFlights) {
            this.numberOfFlights = numberOfFlights;
            return this;
        }

        public UserStatistics build() {
            UserStatistics stats = new UserStatistics();

            stats.setEmailAddress(emailAddress);
            stats.setTotalFlightDuration(totalFlightDuration);
            stats.setNumberOfFlights(numberOfFlights);

            return stats;
        }
    }
}
