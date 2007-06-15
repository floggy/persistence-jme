package net.sourceforge.floggy.persistence.rms;

import java.lang.reflect.Method;
import java.util.Arrays;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;

public abstract class AbstractTest extends TestCase {

    protected PersistableManager manager;

    protected void equals(Object o1, Object o2) {
	if (o1 != null && o2.getClass().isArray()) {
	    Class clazz = o1.getClass().getComponentType();
	    if (clazz == short.class) {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(short[]) o1, (short[]) o2));
	    } else if (clazz == boolean.class) {
		assertTrue("Deveria ser igual(o2s) !", Arrays.equals(
			(boolean[]) o1, (boolean[]) o2));
	    } else if (clazz == byte.class) {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(byte[]) o1, (byte[]) o2));
	    } else if (clazz == char.class) {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(char[]) o1, (char[]) o2));
	    } else if (clazz == double.class) {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(double[]) o1, (double[]) o2));
	    } else if (clazz == int.class) {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(int[]) o1, (int[]) o2));
	    } else if (clazz == float.class) {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(float[]) o1, (float[]) o2));
	    } else if (clazz == long.class) {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(long[]) o1, (long[]) o2));
	    } else {
		assertTrue("Deveria ser igual(arrays) !", Arrays.equals(
			(Object[]) o1, (Object[]) o2));
	    }
	} else {
	    assertEquals("Deveria ser igual!", o1, o2);
	}
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	Class[] classes = new Class[params.length];
	for (int i = 0; i < params.length; i++) {
	    classes[i] = params[i].getClass();
	}
	return classes;
    }

    public Filter getFilter() {
	return new Filter() {
	    public boolean matches(Persistable persistable) {
		Object temp = getValueForSetMethod();
		Object array = null;
		try {
		    array = getX(persistable);
		} catch (Exception e) {
		    throw new RuntimeException(e.getMessage());
		}
		if (temp != null && temp.getClass().isArray()) {
		    Class clazz = temp.getClass().getComponentType();
		    if (clazz == short.class) {
			return Arrays.equals((short[]) temp, (short[]) array);
		    } else if (clazz == boolean.class) {
			return Arrays.equals((boolean[]) temp,
				(boolean[]) array);
		    } else if (clazz == byte.class) {
			return Arrays.equals((byte[]) temp, (byte[]) array);
		    } else if (clazz == char.class) {
			return Arrays.equals((char[]) temp, (char[]) array);
		    } else if (clazz == double.class) {
			return Arrays.equals((double[]) temp, (double[]) array);
		    } else if (clazz == int.class) {
			return Arrays.equals((int[]) temp, (int[]) array);
		    } else if (clazz == float.class) {
			return Arrays.equals((float[]) temp, (float[]) array);
		    } else if (clazz == long.class) {
			return Arrays.equals((long[]) temp, (long[]) array);
		    } else {
			return Arrays.equals((Object[]) temp, (Object[]) array);
		    }
		} else {
		    if (temp == null) {
			return temp == array;
		    } else {
			return temp.equals(array);
		    }
		}
	    }
	};
    }

    public abstract Object getValueForSetMethod();

    protected Object getX(Object object) throws Exception {
	Method method = object.getClass().getMethod("getX", new Class[0]);
	return method.invoke(object, new Object[0]);
    }

    public abstract Persistable newInstance();

    protected void setUp() throws Exception {
	super.setUp();
	manager = PersistableManager.getInstance();
	// File rmsDir = new File(".rms");
	// org.me4se.impl.rms.RecordStoreImpl_file.rmsDir = rmsDir;
    }

    protected void setX(Object object, Object param) throws Exception {
	Method method = object.getClass().getMethod("setX",
		getClassesFromObjects(new Object[] { param }));
	method.invoke(object, new Object[] { param });
    }

    public void testDelete() throws Exception {
	Persistable object = newInstance();
	int id = manager.save(object);
	manager.delete(object);
	object = newInstance();
	try {
	    manager.load(object, id);
	    fail("This object must was deleted!");
	} catch (Exception e) {
	    assertEquals(FloggyException.class, e.getClass());
	}
    }

    public void testDeleteWithNullObject() {
	try {
	    manager.delete(null);
	    fail("A IllegalArgumentException must be throw!");
	} catch (Exception e) {
	    assertEquals(e.getClass(), IllegalArgumentException.class);
	}
    }

    public void testDeleteWithoutSave() {
	Persistable instance = newInstance();
	try {
	    manager.delete(instance);
	    assertTrue("Nothing happend because nothing was saved!", true);
	} catch (Exception e) {
	    e.printStackTrace();
	    fail("No exception should be throwed!");
	}
    }

    public void testFind() throws Exception {
	Persistable object = newInstance();
	setX(object, getValueForSetMethod());
	int id = manager.save(object);
	ObjectSet set = manager.find(object.getClass(), getFilter(), null);
	assertEquals(1, set.size());
	// assertEquals(set.firstId(), id);
	equals(getValueForSetMethod(), getX(set.get(0)));
	manager.delete(object);
    }

    public void testFindPersistableClassIsNotAValidPersistableClass()
	    throws Exception {
	try {
	    manager.find(String.class, null, null);
	    fail("A IllegalArgumentException must be throw!");
	} catch (Exception e) {
	    assertEquals(e.getClass(), IllegalArgumentException.class);
	}
    }

    public void testFindPersistableClassIsNull() throws Exception {
	try {
	    manager.find(null, getFilter(), null);
	    fail("A IllegalArgumentException must be throw!");
	} catch (Exception e) {
	    assertEquals(e.getClass(), IllegalArgumentException.class);
	}
    }

    public void testLoadWithNullObject() {
	try {
	    manager.load(null, 0);
	    fail("A IllegalArgumentException must be throw!");
	} catch (Exception e) {
	    assertEquals(e.getClass(), IllegalArgumentException.class);
	}
    }

    public void testNotNullAttribute() throws Exception {
	Persistable object = newInstance();
	setX(object, getValueForSetMethod());
	int id = manager.save(object);
	assertTrue("Deveria ser diferente de -1!", id != -1);
	object = newInstance();
	manager.load(object, id);
	assertNotNull("N�o deveria ser null!", getX(object));
	equals(getValueForSetMethod(), getX(object));
	manager.delete(object);
    }

    public void testNullAttribute() throws Exception {
	Persistable object = newInstance();
	int id = manager.save(object);
	assertTrue("Deveria ser diferente de -1!", id != -1);
	object = newInstance();
	manager.load(object, id);
	assertNull("Deveria ser null!", getX(object));
	manager.delete(object);
    }

    public void testSaveWithNullObject() {
	try {
	    manager.save(null);
	    fail("A IllegalArgumentException must be throw!");
	} catch (Exception e) {
	    assertEquals(e.getClass(), IllegalArgumentException.class);
	}
    }

}