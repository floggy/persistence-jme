/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.persistence.rms;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Hashtable;

import javax.microedition.rms.InvalidRecordIDException;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

import org.microemu.MIDletBridge;

public abstract class AbstractTest extends TestCase {

	protected PersistableManager manager;

	public AbstractTest() {
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator("target/rms"));
		manager = PersistableManager.getInstance();
	}

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

	protected abstract Class getParameterType();

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
				if (temp != null) {
					if (temp.getClass().isArray()) {
						Class clazz = temp.getClass().getComponentType();
						if (clazz == short.class) {
							return Arrays.equals((short[]) temp,
									(short[]) array);
						} else if (clazz == boolean.class) {
							return Arrays.equals((boolean[]) temp,
									(boolean[]) array);
						} else if (clazz == byte.class) {
							return Arrays.equals((byte[]) temp, (byte[]) array);
						} else if (clazz == char.class) {
							return Arrays.equals((char[]) temp, (char[]) array);
						} else if (clazz == double.class) {
							return Arrays.equals((double[]) temp,
									(double[]) array);
						} else if (clazz == int.class) {
							return Arrays.equals((int[]) temp, (int[]) array);
						} else if (clazz == float.class) {
							return Arrays.equals((float[]) temp,
									(float[]) array);
						} else if (clazz == long.class) {
							return Arrays.equals((long[]) temp, (long[]) array);
						} else {
							return Arrays.equals((Object[]) temp,
									(Object[]) array);
						}
					} else {
						return temp.equals(array);
					}
				} else
					return temp == array;
			}
		};
	}

	public abstract Object getValueForSetMethod();

	public Object getNewValueForSetMethod() {
		return null;
	}

	protected Object getX(Object object) throws Exception {
		Method method = object.getClass().getMethod(getNameForGetMethod(),
				new Class[0]);
		return method.invoke(object, new Object[0]);
	}

	public abstract Persistable newInstance();

	protected void setX(Object object, Object param) throws Exception {
		Method method = object.getClass().getMethod(getNameForSetMethod(),
				new Class[] { getParameterType() });
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

	public void testDeleteAll() throws Exception {
		Persistable object = newInstance();

		manager.deleteAll(object.getClass());
		assertEquals(0, manager.find(object.getClass(), null, null).size());

		manager.save(object);
		// garantido que vai estar aberto
		assertEquals(1, manager.find(object.getClass(), null, null).size());

		manager.deleteAll(object.getClass());
		assertEquals(0, manager.find(object.getClass(), null, null).size());
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
			fail("No exception should be throwed!");
		}
	}

	public void testFind() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);
		try {
			ObjectSet set = manager.find(object.getClass(), null, null);
			assertEquals(1, set.size());
		} finally {
			manager.delete(object);
		}
	}

	public void testFindWithFilter() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		int id = manager.save(object);
		try {
			ObjectSet set = manager.find(object.getClass(), getFilter(), null);
			assertEquals(1, set.size());
			assertEquals(set.getId(0), id);
			equals(getValueForSetMethod(), getX(set.get(0)));
		} finally {
			manager.delete(object);
		}
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

	public void testFR2422928Read() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(object.getClass(), null);
		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertFalse("Should not be empty!", data.isEmpty());
				equals(data.get("x"), getValueForSetMethod());
			}
		} finally {
			manager.delete(object);
			um.finish(object.getClass());
		}
	}

	public void testFR2422928Update() throws Exception {
		Persistable oldObject = newInstance();
		setX(oldObject, getValueForSetMethod());
		manager.save(oldObject);

		Persistable newObject = newInstance();
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(oldObject.getClass(), null);
		try {
			assertEquals(1, enumeration.getSize());
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				setX(newObject, data.get("x"));
				int oldId = manager.getId(oldObject);
				int newId = enumeration.update(newObject);
				assertEquals(oldId, newId);
				
				manager.load(newObject, newId);
				
				equals(getX(oldObject), getX(newObject));
				
			}
		} finally {
			um.finish(oldObject.getClass());
			manager.delete(newObject);
		}
	}

	public void testFR2422928Delete() throws Exception {
		Persistable object = newInstance();
		int recordId = manager.save(object);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(object.getClass(), null);
		try {
			while (enumeration.hasMoreElements()) {
				enumeration.nextElement();
				int newRecordId = enumeration.delete();
				assertEquals(recordId, newRecordId);
			}
			try {
				manager.load(newInstance(), recordId);
				fail("It should throw a InvalidRecordIDException");
			} catch (Exception ex) {
				assertEquals(InvalidRecordIDException.class.getName(), ex
						.getMessage());
			}
		} finally {
 			um.finish(object.getClass());
		}
	}

	public void testIsPersisted() throws Exception {
		Persistable object = newInstance();
		boolean isPersisted = manager.isPersisted(object);
		assertFalse(isPersisted);
		manager.save(object);
		try {
			isPersisted = manager.isPersisted(object);
			assertTrue(isPersisted);
		} finally {
			manager.delete(object);
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
		try {
			assertTrue("Deveria ser diferente de -1!", id != -1);
			object = newInstance();
			manager.load(object, id);
			assertNotNull("Não deveria ser null!", getX(object));
			equals(getValueForSetMethod(), getX(object));
		} finally {
			manager.delete(object);
		}
	}

	public void testNullAttribute() throws Exception {
		Persistable object = newInstance();
		int id = manager.save(object);
		try {
			assertTrue("Deveria ser diferente de -1!", id != -1);
			object = newInstance();
			manager.load(object, id);
			assertNull("Deveria ser null!", getX(object));
		} finally {
			manager.delete(object);
		}
	}

	public void testSaveWithNullObject() {
		try {
			manager.save(null);
			fail("A IllegalArgumentException must be throw!");
		} catch (Exception e) {
			assertEquals(e.getClass(), IllegalArgumentException.class);
		}
	}

	public void testSaveAndEdit() throws Exception {
		Persistable object = newInstance();
		int id = manager.save(object);
		try {
			assertTrue("Deveria ser diferente de -1!", id != -1);
			object = newInstance();
			manager.load(object, id);
			setX(object, getNewValueForSetMethod());
			int tempId = manager.save(object);
			assertEquals(id, tempId);
		} finally {
			manager.delete(object);
		}
	}

	public void testSaveWithRecordIdThatDontExist() {
		try {
			manager.load(newInstance(), 123234);
			fail("A RecordStore exception must be throwed!");
		} catch (Exception ex) {
			assertEquals(InvalidRecordIDException.class.getName(), ex
					.getMessage());
		}
	}
	
	public void testFR2816948() throws Exception {
		Persistable object = newInstance();
		try {
			int id = manager.getId(object);
			assertEquals(-1, id);

			id = manager.save(object);
			assertTrue("Deveria ser diferente de -1!", id != -1);

			int temp = manager.getId(object);
			assertEquals(id, temp);
		} finally {
			manager.delete(object);
		}
	}

	public String getNameForGetMethod() {
		return "getX";
	}

	public String getNameForSetMethod() {
		return "setX";
	}

}
