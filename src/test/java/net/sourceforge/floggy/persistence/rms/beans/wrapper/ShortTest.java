package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestShort;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class ShortTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestShort();
    }

    public Object getValueForSetMethod() {
	return new Short((short) 23);
    }

}
