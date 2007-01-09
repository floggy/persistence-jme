package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestDouble;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class DoubleTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestDouble();
    }

    public Object getValueForSetMethod() {
	return new Double(23434560.897987987);
    }

}
