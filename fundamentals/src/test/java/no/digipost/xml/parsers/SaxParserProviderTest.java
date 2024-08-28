package no.digipost.xml.parsers;

import org.junit.jupiter.api.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static no.digipost.xml.transform.sax.SaxInputSources.fromInputStreamPreventClose;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static uk.co.probablyfine.matchers.Java8Matchers.where;

class SaxParserProviderTest {

    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

    private static final String A_BILLION_LAUGHS_DOCTYPE_DECLARATION =
            "<!DOCTYPE lolz [<!ENTITY lol \"lol\"><!ELEMENT lolz (#PCDATA)>\n" +
            "  <!ENTITY lol1 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "  <!ENTITY lol2 \"&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;&lol1;\">\n" +
            "  <!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\">\n" +
            "  <!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\">\n" +
            "  <!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\">\n" +
            "  <!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\">\n" +
            "  <!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\">\n" +
            "  <!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\">\n" +
            "  <!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\">\n" +
            "]>";

    @Test
    void preventsBillionLaughsAttack() throws IOException {

        String evilXml =
                XML_DECLARATION + "\n" +
                A_BILLION_LAUGHS_DOCTYPE_DECLARATION + "\n" +
                "<root><element>&lol1;</element></root>";

        XMLReader xmlReader = SaxParserProvider.createSecuredProvider().createXMLReader();
        try (InputStream xml = new ByteArrayInputStream(evilXml.getBytes(UTF_8))) {
            InputSource inputSource = fromInputStreamPreventClose(xml);
            Exception thrown = assertThrows(SAXParseException.class, () -> xmlReader.parse(inputSource));
            assertThat(thrown, where(Exception::getMessage, containsStringIgnoringCase("DOCTYPE is disallowed")));
        }
    }

}
