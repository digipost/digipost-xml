package no.digipost.org.jvnet.jaxb2_commons.lang;

import org.jvnet.jaxb2_commons.lang.DefaultToStringStrategy;
import org.jvnet.jaxb2_commons.locator.ObjectLocator;

public class SuccinctToStringStrategy extends DefaultToStringStrategy {

    @Override
    public boolean isUseIdentityHashCode() {
        return false;
    }

    @Override
    public boolean isUseDefaultFieldValueMarkers() {
        return false;
    }

    @Override
    protected void appendClassName(StringBuilder toString, Object object) {
        if (object != null) {
            toString.append(object.getClass().getSimpleName());
        }
    }

    @Override
    public StringBuilder appendField(ObjectLocator parentLocator, Object parent, String fieldName, StringBuilder buffer, Object value, boolean valueSet) {
        if (valueSet) {
            return super.appendField(parentLocator, parent, fieldName, buffer, value, valueSet);
        }
        return buffer;
    }

    private static final SuccinctToStringStrategy INSTANCE = new SuccinctToStringStrategy();

    public static SuccinctToStringStrategy getInstance() {
        return INSTANCE;
    }

}
