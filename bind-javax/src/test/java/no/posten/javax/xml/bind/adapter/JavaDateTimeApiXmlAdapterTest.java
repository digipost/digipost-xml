package no.posten.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

class JavaDateTimeApiXmlAdapterTest {

    @Test
    void handlesNull() {
        JavaDateTimeApiXmlAdapter<?> adapter = new JavaDateTimeApiXmlAdapter<>(
                __ -> fail("marshal adapter should not be used"),
                (__, ___) -> fail("unmarshal adapter should not be used"));

        assertAll(
                () -> assertThat(adapter.marshal(null), nullValue()),
                () -> assertThat(adapter.unmarshal(null), nullValue()));
    }
}
