package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyString;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StringTest extends AbstractTest {

    public Persistable newInstance() {
	return new FloggyString();
    }

    public Object getValueForSetMethod() {
	return "floggy-framework";
    }

}
