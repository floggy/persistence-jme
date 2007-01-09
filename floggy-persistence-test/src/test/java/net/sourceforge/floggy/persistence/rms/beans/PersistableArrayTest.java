package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyPersistable;
import net.sourceforge.floggy.persistence.beans.FloggyPersistableArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class PersistableArrayTest extends AbstractTest {

    static FloggyPersistable[] persistables = new FloggyPersistable[2];

    static {
	FloggyPersistable persistable = new FloggyPersistable();
	persistable.setX(new FloggyPersistable());
	persistables[0] = persistable;
	persistables[1] = new FloggyPersistable();
    }

    public Persistable newInstance() {
	return new FloggyPersistableArray();
    }

    public Object getValueForSetMethod() {
	return persistables;
    }

}
