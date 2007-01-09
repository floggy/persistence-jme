package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyPersistable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class PersistableTest extends AbstractTest {

    public final static FloggyPersistable persistable = new FloggyPersistable();

    public Persistable newInstance() {
	return new FloggyPersistable();
    }

    public Object getValueForSetMethod() {
	return persistable;
    }

}
