package no.digipost.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MILLIS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class InstantXmlAdapterTest {

    private final InstantXmlAdapter adapter = new InstantXmlAdapter();

    @Test
    public void marshall_unmarshall_roundtrip_yields_equal_objects() {
        Instant now = Instant.now().truncatedTo(MILLIS);
        assertThat(adapter.unmarshal(adapter.marshal(now)), is(now));
    }

}
