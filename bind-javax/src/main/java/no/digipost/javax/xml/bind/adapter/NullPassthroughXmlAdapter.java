package no.digipost.javax.xml.bind.adapter;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import java.util.function.Function;

/**
 * Base class for creating {@link XmlAdapter}s where {@code null} values
 * are passed through, and not evaluated by marshalling/unmarshalling logic.
 *
 * @see XmlAdapter
 */
public class NullPassthroughXmlAdapter<ValueType, BoundType> extends XmlAdapter<ValueType, BoundType> {

    private final Function<? super BoundType, ? extends ValueType> marshal;
    private final Function<? super ValueType, ? extends BoundType> unmarshal;

    public NullPassthroughXmlAdapter(Function<? super BoundType, ? extends ValueType> marshal, Function<? super ValueType, ? extends BoundType> unmarshal) {
        this.marshal = marshal;
        this.unmarshal = unmarshal;
    }

    @Override
    public final ValueType marshal(BoundType boundValue) {
        return boundValue != null ? marshal.apply(boundValue) : null;
    }

    @Override
    public final BoundType unmarshal(ValueType xmlValue) {
        return xmlValue != null ? unmarshal.apply(xmlValue) : null;
    }

}
