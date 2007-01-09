package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStringArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StringArrayTest extends AbstractTest {

    public Persistable newInstance() {
	return new FloggyStringArray();
    }

    public Object getValueForSetMethod() {
	return new String[] { "floggy-framework", "batim" };
    }

}
