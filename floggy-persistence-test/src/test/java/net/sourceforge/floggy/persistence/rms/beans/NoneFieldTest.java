package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyNoneFields;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class NoneFieldTest extends AbstractTest {

    public Persistable newInstance() {
	return new FloggyNoneFields();
    }

    public Object getValueForSetMethod() {
	return null;
    }

    public void testNullAttribute() throws Exception {
	Persistable object = newInstance();
	int id = manager.save(object);
	assertTrue("Deveria ser diferente de -1!", id != -1);
	object = newInstance();
	manager.load(object, id);
	manager.delete(object);
    }

    public void testNotNullAttribute() throws Exception {
	Persistable object1 = newInstance();
	int id = manager.save(object1);
	assertTrue("Deveria ser diferente de -1!", id != -1);
	Persistable object2 = newInstance();
	manager.load(object2, id);
	assertEquals(object1, object2);
	manager.delete(object1);
    }

    public void testFind() throws Exception {
    }

}
