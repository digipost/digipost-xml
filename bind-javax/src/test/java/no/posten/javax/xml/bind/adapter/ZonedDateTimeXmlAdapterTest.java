package no.posten.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class ZonedDateTimeXmlAdapterTest {

    private final ZonedDateTimeXmlAdapter adapter = new ZonedDateTimeXmlAdapter();

    @Test
    public void marshall_unmarshall_roundtrip_yields_equal_objects() {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("GMT-8")).truncatedTo(MILLIS);
        assertThat(adapter.unmarshal(adapter.marshal(now)), is(now));
    }

    @Test
    public void unmarshall_yields_datetime_with_region_based_zoneId_replaced_with_GMT_offset() {
        ZoneId newYorkZone = ZoneId.of("America/New_York");
        ZonedDateTime rightNowInAmerica = ZonedDateTime.now(newYorkZone).truncatedTo(MILLIS);
        String xmlDateTimeString = adapter.marshal(rightNowInAmerica);

        boolean daylightSavings = newYorkZone.getRules().isDaylightSavings(rightNowInAmerica.toInstant());
        ZoneId gmtZone = daylightSavings ? ZoneId.of("GMT-4") : ZoneId.of("GMT-5");
        assertThat(adapter.unmarshal(xmlDateTimeString), is(rightNowInAmerica.withZoneSameInstant(gmtZone)));
    }

    @Test
    void handlesNull() {
        assertThat(adapter.marshal(null), nullValue());
    }
}
