package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public abstract class PrimitiveAbstractTest extends AbstractTest {

    protected abstract Class[] getClassesFromObjects(Object[] params);

    public void testNullAttribute() throws Exception {
	Persistable object = newInstance();
	int id = manager.save(object);
	assertTrue("Deveria ser diferente de -1!", id != -1);
	object = newInstance();
	manager.load(object, id);
	assertEquals("Deveria ser igual (valor default)!", getDefaultValue(),
		getX(object));
	manager.delete(object);
    }

    public abstract Object getDefaultValue();
}
