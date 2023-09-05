package models;

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import static org.apache.flink.types.PojoTestUtils.assertSerializedAsPojo;
import static org.junit.jupiter.api.Assertions.*;

class SkyOneAirlinesFlightDataTest {

    ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Test
    void theClass_shouldBeSerializableAsAPOJO() {
        assertSerializedAsPojo(SkyOneAirlinesFlightData.class);
    }

    @Test
    public void setters_shouldPopulateExpectedFields() {
        SkyOneAirlinesFlightData expected = new TestHelpers.SkyOneBuilder().build();
        SkyOneAirlinesFlightData actual = new SkyOneAirlinesFlightData();
        actual.setEmailAddress(expected.getEmailAddress());
        actual.setFlightDepartureTime(expected.getFlightDepartureTime());
        actual.setIataDepartureCode(expected.getIataDepartureCode());
        actual.setFlightArrivalTime(expected.getFlightArrivalTime());
        actual.setIataArrivalCode(expected.getIataArrivalCode());
        actual.setFlightNumber(expected.getFlightNumber());
        actual.setConfirmation(expected.getConfirmation());

        assertEquals(expected.getEmailAddress(), actual.getEmailAddress());
        assertEquals(expected.getFlightDepartureTime(), actual.getFlightDepartureTime());
        assertEquals(expected.getIataDepartureCode(), actual.getIataDepartureCode());
        assertEquals(expected.getFlightArrivalTime(), actual.getFlightArrivalTime());
        assertEquals(expected.getIataArrivalCode(), actual.getIataArrivalCode());
        assertEquals(expected.getFlightNumber(), actual.getFlightNumber());
        assertEquals(expected.getConfirmation(), actual.getConfirmation());
    }

    @Test
    public void equals_shouldReturnTrue_forTwoEquivalentFlights() {
        SkyOneAirlinesFlightData flight1 = new TestHelpers.SkyOneBuilder().build();
        SkyOneAirlinesFlightData flight2 = new SkyOneAirlinesFlightData();
        flight2.setEmailAddress(flight1.getEmailAddress());
        flight2.setFlightDepartureTime(flight1.getFlightDepartureTime());
        flight2.setIataDepartureCode(flight1.getIataDepartureCode());
        flight2.setFlightArrivalTime(flight1.getFlightArrivalTime());
        flight2.setIataArrivalCode(flight1.getIataArrivalCode());
        flight2.setFlightNumber(flight1.getFlightNumber());
        flight2.setConfirmation(flight1.getConfirmation());

        assertNotSame(flight1, flight2);
        assertEquals(flight1, flight2);
        assertEquals(flight1.hashCode(), flight2.hashCode());
    }

    @Test
    public void equals_shouldReturnFalse_forTwoDifferentFlights() {
        SkyOneAirlinesFlightData flight1 = new TestHelpers.SkyOneBuilder().build();
        SkyOneAirlinesFlightData flight2 = new TestHelpers.SkyOneBuilder().build();

        assertNotSame(flight1, flight2);
        assertNotEquals(flight1, flight2);
        assertNotEquals(flight1.hashCode(), flight2.hashCode());
    }

    @Test
    public void toString_shouldReturnTheExpectedResults() {
        SkyOneAirlinesFlightData flightData = new TestHelpers.SkyOneBuilder().build();

        String expected = "SkyOneAirlinesFlightData{" +
            "emailAddress='" + flightData.getEmailAddress() + '\'' +
            ", flightDepartureTime=" + flightData.getFlightDepartureTime() +
            ", iataDepartureCode='" + flightData.getIataDepartureCode() + '\'' +
            ", flightArrivalTime=" + flightData.getFlightArrivalTime() +
            ", iataArrivalCode='" + flightData.getIataArrivalCode() + '\'' +
            ", flightNumber='" + flightData.getFlightNumber() + '\'' +
            ", confirmation='" + flightData.getConfirmation() + '\'' +
            '}';

        assertEquals(expected, flightData.toString());

    }

    @Test
    public void serializer_shouldSerializeAndDeserializeTheCorrectObject() throws Exception {
        SkyOneAirlinesFlightData original = new TestHelpers.SkyOneBuilder().build();

        String serialized = mapper.writeValueAsString(original);
        SkyOneAirlinesFlightData deserialized = mapper.readValue(serialized, SkyOneAirlinesFlightData.class);

        assertSerializedAsPojo(SkyOneAirlinesFlightData.class);
        assertEquals(original, deserialized);
    }

    @Test
    public void serializer_shouldHandleUnknownFields() throws Exception {
        String json = "{\"emailAddress\":\"LJNZGYPIER@email.com\",\"flightDepartureTime\":\"2023-10-16T22:25:00.000Z\",\"iataDepartureCode\":\"LAS\",\"flightArrivalTime\":\"2023-10-17T09:38:00.000Z\",\"iataArrivalCode\":\"BOS\",\"flightNumber\":\"SKY1522\",\"confirmation\":\"SKY1OUJUUK\",\"unknownField\":\"ignore\"}";

        SkyOneAirlinesFlightData object = mapper.readValue(json, SkyOneAirlinesFlightData.class);

        assertInstanceOf(SkyOneAirlinesFlightData.class, object);
    }
}