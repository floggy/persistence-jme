package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestFloat;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class FloatTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestFloat();
    }

    public Object getValueForSetMethod() {
	return new Float((float) 23.0987);
    }

}
