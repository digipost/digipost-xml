package no.digipost.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.fail;

class NullPassthroughXmlAdapterTest {

    @Test
    void handlesNull() {
        NullPassthroughXmlAdapter<?, ?> adapter = new NullPassthroughXmlAdapter<>(
                __ -> fail("marshal function should not be applied"),
                __ -> fail("unmarshal function should not be applied"));

        assertAll(
                () -> assertThat(adapter.marshal(null), nullValue()),
                () -> assertThat(adapter.unmarshal(null), nullValue()));
    }
}
