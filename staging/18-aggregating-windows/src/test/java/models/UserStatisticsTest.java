package models;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.apache.flink.types.PojoTestUtils.assertSerializedAsPojo;
import static org.junit.jupiter.api.Assertions.*;

class UserStatisticsTest {

    @Test
    void theClass_shouldBeSerializableAsAPOJO() {
        assertSerializedAsPojo(UserStatistics.class);
    }

    @Test
    public void constructor_shouldCreateStatisticsUsingFlightData() {
        FlightData flightData = new TestHelpers.FlightDataBuilder().build();
        UserStatistics stats = new UserStatistics(flightData);

        Duration expectedDuration = Duration.between(flightData.getDepartureTime(), flightData.getArrivalTime());

        assertEquals(flightData.getEmailAddress(), stats.getEmailAddress());
        assertEquals(expectedDuration, stats.getTotalFlightDuration());
        assertEquals(1, stats.getNumberOfFlights());
    }

    @Test
    public void setters_shouldPopulateExpectedFields() {
        UserStatistics expected = new TestHelpers.UserStatisticsBuilder().build();
        UserStatistics actual = new UserStatistics();
        actual.setEmailAddress(expected.getEmailAddress());
        actual.setTotalFlightDuration(expected.getTotalFlightDuration());
        actual.setNumberOfFlights(expected.getNumberOfFlights());

        assertEquals(expected.getEmailAddress(), actual.getEmailAddress());
        assertEquals(expected.getTotalFlightDuration(), actual.getTotalFlightDuration());
        assertEquals(expected.getNumberOfFlights(), actual.getNumberOfFlights());
    }

    @Test
    public void equals_shouldReturnTrue_forTwoEquivalentUserStatistics() {
        UserStatistics stats1 = new TestHelpers.UserStatisticsBuilder().build();
        UserStatistics stats2 = new UserStatistics();
        stats2.setEmailAddress(stats1.getEmailAddress());
        stats2.setTotalFlightDuration(stats1.getTotalFlightDuration());
        stats2.setNumberOfFlights(stats1.getNumberOfFlights());

        assertNotSame(stats1, stats2);
        assertEquals(stats1, stats2);
        assertEquals(stats1.hashCode(), stats2.hashCode());
    }

    @Test
    public void equals_shouldReturnFalse_forTwoDifferentUserStatistics() {
        UserStatistics stats1 = new TestHelpers.UserStatisticsBuilder().build();
        UserStatistics stats2 = new TestHelpers.UserStatisticsBuilder().build();

        assertNotSame(stats1, stats2);
        assertNotEquals(stats1, stats2);
        assertNotEquals(stats1.hashCode(), stats2.hashCode());
    }

    @Test
    public void merge_shouldMergeTheUserStatistics() {
        UserStatistics stats1 = new TestHelpers.UserStatisticsBuilder().build();
        UserStatistics stats2 = new TestHelpers.UserStatisticsBuilder()
            .setEmailAddress(stats1.getEmailAddress())
            .build();

        UserStatistics merged = stats1.merge(stats2);

        assertEquals(stats1.getEmailAddress(), merged.getEmailAddress());
        assertEquals(stats1.getTotalFlightDuration().plus(stats2.getTotalFlightDuration()), merged.getTotalFlightDuration());
        assertEquals(stats1.getNumberOfFlights() + stats2.getNumberOfFlights(), merged.getNumberOfFlights());
    }

    @Test
    public void merge_shouldFailIfTheEmailAddressesAreDifferent() {
        UserStatistics stats1 = new TestHelpers.UserStatisticsBuilder().build();
        UserStatistics stats2 = new TestHelpers.UserStatisticsBuilder().setEmailAddress("notthesame@email.com").build();

        assertNotEquals(stats1.getEmailAddress(), stats2.getEmailAddress());

        assertThrows(AssertionError.class, () -> stats1.merge(stats2));
    }
}