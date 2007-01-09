package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.FloggyBoolean;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class BooleanTest extends AbstractTest {

    static Boolean[] att = new Boolean[] { Boolean.FALSE, Boolean.TRUE,
	    Boolean.FALSE };

    public Persistable newInstance() {
	return new FloggyBoolean();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
