package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyTransient;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class TransientTest extends AbstractTest {

    static final Object object = new Object();

    public Persistable newInstance() {
	return new FloggyTransient();
    }

    public void testNotNullAttribute() throws Exception {
	// como o atributo não vai ser salvo ele tem q retornar null!!!
	super.testNullAttribute();
    }

    public Object getValueForSetMethod() {
	return object;
    }

    public void testFind() throws Exception {
	Persistable object = newInstance();
	setX(object, getValueForSetMethod());
	manager.save(object);
	ObjectSet set = manager.find(object.getClass(), getFilter(), null);
	assertEquals(0, set.size());
	manager.delete(object);
    }

}
