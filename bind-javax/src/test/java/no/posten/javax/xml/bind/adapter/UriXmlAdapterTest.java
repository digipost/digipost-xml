package no.posten.javax.xml.bind.adapter;

import org.junit.jupiter.api.Test;

import java.net.URI;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static uk.co.probablyfine.matchers.Java8Matchers.where;

class UriXmlAdapterTest {

    private final UriXmlAdapter adapter = new UriXmlAdapter();

    @Test
    void convertsToUri() {
        assertThat("https://posten.no", where(adapter::unmarshal, is(URI.create("https://posten.no"))));
    }

    @Test
    void convertsToStringValue() {
        assertThat(URI.create("https://posten.no"), where(adapter::marshal, is("https://posten.no")));
        assertThat(URI.create("https://nårvægian.charactørs.no"), where(adapter::marshal, is("https://n%C3%A5rv%C3%A6gian.charact%C3%B8rs.no")));
    }

}
