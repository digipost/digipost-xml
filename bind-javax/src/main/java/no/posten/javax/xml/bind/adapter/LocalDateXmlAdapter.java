package no.posten.javax.xml.bind.adapter;

import javax.xml.bind.DatatypeConverter;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Calendar;

import static java.time.format.DateTimeFormatter.ISO_LOCAL_DATE;

public class LocalDateXmlAdapter extends NullPassthroughXmlAdapter<String, LocalDate> {

    LocalDateXmlAdapter() {
        super(
                ISO_LOCAL_DATE::format,
                xsDate -> {
                    Calendar calendar = DatatypeConverter.parseDate(xsDate);
                    return ZonedDateTime.ofInstant(calendar.toInstant(), calendar.getTimeZone().toZoneId()).toLocalDate();
                });
    }

}
