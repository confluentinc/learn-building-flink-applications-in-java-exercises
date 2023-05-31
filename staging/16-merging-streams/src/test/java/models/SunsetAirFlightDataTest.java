package models;

import org.junit.jupiter.api.Test;

import static org.apache.flink.types.PojoTestUtils.assertSerializedAsPojo;
import static org.junit.jupiter.api.Assertions.*;

class SunsetAirFlightDataTest {

    @Test
    void theClass_shouldBeSerializableAsAPOJO() {
        assertSerializedAsPojo(SunsetAirFlightData.class);
    }

    @Test
    public void setters_shouldPopulateExpectedFields() {
        SunsetAirFlightData expected = new TestHelpers.SunsetBuilder().build();
        SunsetAirFlightData actual = new SunsetAirFlightData();
        actual.setCustomerEmailAddress(expected.getCustomerEmailAddress());
        actual.setDepartureTime(expected.getDepartureTime());
        actual.setDepartureAirport(expected.getDepartureAirport());
        actual.setArrivalTime(expected.getArrivalTime());
        actual.setArrivalAirport(expected.getArrivalAirport());
        actual.setFlightId(expected.getFlightId());
        actual.setReferenceNumber(expected.getReferenceNumber());

        assertEquals(expected.getCustomerEmailAddress(), actual.getCustomerEmailAddress());
        assertEquals(expected.getDepartureTime(), actual.getDepartureTime());
        assertEquals(expected.getDepartureAirport(), actual.getDepartureAirport());
        assertEquals(expected.getArrivalTime(), actual.getArrivalTime());
        assertEquals(expected.getArrivalAirport(), actual.getArrivalAirport());
        assertEquals(expected.getFlightId(), actual.getFlightId());
        assertEquals(expected.getReferenceNumber(), actual.getReferenceNumber());
    }
    @Test
    public void equals_shouldReturnTrue_forTwoEquivalentFlights() {
        SunsetAirFlightData flight1 = new TestHelpers.SunsetBuilder().build();
        SunsetAirFlightData flight2 = new SunsetAirFlightData();
        flight2.setCustomerEmailAddress(flight1.getCustomerEmailAddress());
        flight2.setDepartureTime(flight1.getDepartureTime());
        flight2.setDepartureAirport(flight1.getDepartureAirport());
        flight2.setArrivalTime(flight1.getArrivalTime());
        flight2.setArrivalAirport(flight1.getArrivalAirport());
        flight2.setFlightId(flight1.getFlightId());
        flight2.setReferenceNumber(flight1.getReferenceNumber());

        assertNotSame(flight1, flight2);
        assertEquals(flight1, flight2);
        assertEquals(flight1.hashCode(), flight2.hashCode());
    }

    @Test
    public void equals_shouldReturnFalse_forTwoDifferentFlights() {
        SunsetAirFlightData flight1 = new TestHelpers.SunsetBuilder().build();
        SunsetAirFlightData flight2 = new TestHelpers.SunsetBuilder().build();

        assertNotSame(flight1, flight2);
        assertNotEquals(flight1, flight2);
        assertNotEquals(flight1.hashCode(), flight2.hashCode());
    }

    @Test
    public void toFlightData_shouldConvertToAFlightDataObject() {
        SunsetAirFlightData skyOne = new TestHelpers.SunsetBuilder().build();
        FlightData expected = new FlightData();
        expected.setEmailAddress(skyOne.getCustomerEmailAddress());
        expected.setDepartureTime(skyOne.getDepartureTime());
        expected.setDepartureAirportCode(skyOne.getDepartureAirport());
        expected.setArrivalTime(skyOne.getArrivalTime());
        expected.setArrivalAirportCode(skyOne.getArrivalAirport());
        expected.setFlightNumber(skyOne.getFlightId());
        expected.setConfirmationCode(skyOne.getReferenceNumber());

        FlightData actual = skyOne.toFlightData();

        assertEquals(expected, actual);
    }

}