package no.digipost.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

class LongSecondsXmlAdapterTest {

    @Test
    void marshalUnmarshalRoundtrip() {
        LongSecondsXmlAdapter adapter = new LongSecondsXmlAdapter();
        Duration oneMinute = Duration.ofMinutes(1);
        Long seconds = adapter.marshal(oneMinute);
        assertAll(
                () -> assertThat(seconds, is(60L)),
                () -> assertThat(adapter.unmarshal(seconds), is(oneMinute)));
    }
}
