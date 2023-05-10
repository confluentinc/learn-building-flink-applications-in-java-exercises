package datagen;

import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataGenerator {
    private static Random random = new Random(System.currentTimeMillis());
    private static List<String> users = Stream
            .generate(() -> generateString(5)+"@email.com")
            .limit(100)
            .collect(Collectors.toList());

    private static String generateAirportCode() {
        String[] airports = new String[] {
                "ATL", "DFW", "DEN", "ORD", "LAX", "CLT", "MCO", "LAS", "PHX", "MIA",
                "SEA", "IAH", "JFK", "EWR", "FLL", "MSP", "SFO", "DTW", "BOS", "SLC",
                "PHL", "BWI", "TPA", "SAN", "LGA", "MDW", "BNA", "IAD", "DCA", "AUS"
        };

        return airports[random.nextInt(airports.length)];
    }

    private static String generateString(int size) {
        final String alphaString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        StringBuilder sb = new StringBuilder(size);

        for(int i = 0; i < size; i++) {
            final int index = random.nextInt(alphaString.length());
            sb.append(alphaString.charAt(index));
        }

        return sb.toString();
    }

    private static String generateEmail() {
        return users.get(random.nextInt(users.size()));
    }

    private static ZonedDateTime generateDepartureTime() {
        return LocalDate.now()
                .plusDays(random.nextInt(365))
                .atTime(random.nextInt(24), random.nextInt(60))
                .atZone(ZoneId.of("UTC"));
    }

    private static ZonedDateTime generateArrivalTime(ZonedDateTime departure) {
        return departure
                .plusHours(random.nextInt(15))
                .plusMinutes(random.nextInt(60));
    }

    public static SkyOneAirlinesFlightData generateSkyOneAirlinesFlightData() {
        SkyOneAirlinesFlightData flightData = new SkyOneAirlinesFlightData();

        flightData.setEmailAddress(generateEmail());
        flightData.setFlightDepartureTime(generateDepartureTime());
        flightData.setIataDepartureCode(generateAirportCode());
        flightData.setFlightArrivalTime(generateArrivalTime(flightData.getFlightDepartureTime()));
        flightData.setIataArrivalCode(generateAirportCode());
        flightData.setFlightNumber("SKY1"+random.nextInt(1000));
        flightData.setConfirmation("SKY1"+generateString(6));
        flightData.setTicketPrice(500+random.nextInt(1000));
        flightData.setAircraft("Aircraft"+generateString(3));
        flightData.setBookingAgencyEmail(generateEmail());

        return flightData;
    }

    public static SunsetAirFlightData generateSunsetAirFlightData() {
        SunsetAirFlightData flightData = new SunsetAirFlightData();

        flightData.setCustomerEmailAddress(generateEmail());
        flightData.setDepartureTime(generateDepartureTime());
        flightData.setDepartureAirport(generateAirportCode());
        flightData.setArrivalTime(generateArrivalTime(flightData.getDepartureTime()));
        flightData.setArrivalAirport(generateAirportCode());
        flightData.setFlightDuration(Duration.between(flightData.getDepartureTime(), flightData.getArrivalTime()));
        flightData.setFlightId("SUN"+random.nextInt(1000));
        flightData.setReferenceNumber("SUN"+generateString(8));
        flightData.setTotalPrice(new BigDecimal(300+random.nextInt(1500)));
        flightData.setAircraftDetails("Aircraft"+generateString(4));

        return flightData;
    }
}
