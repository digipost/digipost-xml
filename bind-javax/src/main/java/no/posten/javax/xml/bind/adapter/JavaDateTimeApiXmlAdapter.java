package no.posten.javax.xml.bind.adapter;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.BiFunction;
import java.util.function.Function;

class JavaDateTimeApiXmlAdapter<T extends Temporal> extends XmlAdapter<String, T> {

    private final Function<? super T, ZonedDateTime> onMarshalToXml;
    private final BiFunction<? super Instant, ? super ZoneId, ? extends T> onUnmarshalFromXml;

    JavaDateTimeApiXmlAdapter(Function<? super T, ZonedDateTime> onMarshalToXml, BiFunction<? super Instant, ? super ZoneId, ? extends T> onUnmarshalFromXml) {
        this.onMarshalToXml = onMarshalToXml;
        this.onUnmarshalFromXml = onUnmarshalFromXml;
    }

    @Override
    public final T unmarshal(String value) {
        if (value == null) {
            return null;
        }
        Calendar parsed = DatatypeConverter.parseDate(value);
        return onUnmarshalFromXml.apply(parsed.toInstant(), parsed.getTimeZone().toZoneId());
    }

    @Override
    public final String marshal(T temporalValue) {
        if (temporalValue == null) {
            return null;
        }
        ZonedDateTime zonedDateTime = onMarshalToXml.apply(temporalValue);
        return DatatypeConverter.printDateTime(GregorianCalendar.from(zonedDateTime));
    }

}
