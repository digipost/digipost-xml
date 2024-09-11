package no.digipost.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static uk.co.probablyfine.matchers.Java8Matchers.where;

class LocalDateXmlAdapterTest {

    @Test
    void marshalUnmarshalRoundtrip() {
        LocalDateXmlAdapter adapter = new LocalDateXmlAdapter();
        LocalDate aug31th = LocalDate.of(2019, 8, 31);
        String iso8601Aug31th = adapter.marshal(aug31th);
        assertAll(
                () -> assertThat(iso8601Aug31th, is("2019-08-31")),
                () -> assertThat(iso8601Aug31th, where(adapter::unmarshal, is(aug31th))));
    }
}
