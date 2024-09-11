package no.digipost.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.time.ZoneId;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class OffsetDateTimeXmlAdapterTest {

    private final OffsetDateTimeXmlAdapter adapter = new OffsetDateTimeXmlAdapter();

    @Test
    public void marshall_unmarshall_roundtrip_yields_equal_objects() {
        OffsetDateTime now = OffsetDateTime.now(ZoneId.of("GMT-8")).truncatedTo(MILLIS);
        assertThat(adapter.unmarshal(adapter.marshal(now)), is(now));
    }

    @Test
    public void unmarshall_yields_datetime_with_region_based_zoneId_replaced_with_GMT_offset() {
        ZoneId newYorkZone = ZoneId.of("America/New_York");
        OffsetDateTime rightNowInAmerica = OffsetDateTime.now(newYorkZone).truncatedTo(MILLIS);
        String xmlDateTimeString = adapter.marshal(rightNowInAmerica);

        assertThat(adapter.unmarshal(xmlDateTimeString), is(rightNowInAmerica));
    }
}
