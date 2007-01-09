package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestLong;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class LongTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestLong();
    }

    public Object getValueForSetMethod() {
	return new Long(456789);
    }

}
