package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyTransientArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class TransientArrayTest extends AbstractTest {

    public Persistable newInstance() {
	return new FloggyTransientArray();
    }

    public void testNotNullAttribute() throws Exception {
	// como o atributo não vai ser salvo ele tem q retornar null!!!
	super.testNullAttribute();
    }

    public Object getValueForSetMethod() {
	return new Object[] { "floggy-framework", TransientTest.object };
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
