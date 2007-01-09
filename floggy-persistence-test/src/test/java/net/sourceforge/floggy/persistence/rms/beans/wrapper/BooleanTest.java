package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestBoolean;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class BooleanTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestBoolean();
    }

    public Object getValueForSetMethod() {
	return Boolean.FALSE;
    }

}
