package no.posten.javax.xml.bind.adapter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.BiFunction;
import java.util.function.Function;

import static javax.xml.bind.DatatypeConverter.parseDateTime;
import static javax.xml.bind.DatatypeConverter.printDateTime;

class JavaDateTimeApiXmlAdapter<T extends Temporal> extends NullPassthroughXmlAdapter<String, T> {

    JavaDateTimeApiXmlAdapter(Function<? super T, ZonedDateTime> onMarshalToXml, BiFunction<? super Instant, ? super ZoneId, ? extends T> onUnmarshalFromXml) {
        super(
                temporal -> printDateTime(GregorianCalendar.from(onMarshalToXml.apply(temporal))),
                xmlValue -> {
                    Calendar parsed = parseDateTime(xmlValue);
                    return onUnmarshalFromXml.apply(parsed.toInstant(), parsed.getTimeZone().toZoneId());
                });
    }

}
