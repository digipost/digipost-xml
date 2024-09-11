package no.digipost.javax.xml.bind.adapter;

import java.time.Duration;

public class LongSecondsXmlAdapter extends NullPassthroughXmlAdapter<Long, Duration> {

    public LongSecondsXmlAdapter() {
        super(Duration::getSeconds, Duration::ofSeconds);
    }

}
