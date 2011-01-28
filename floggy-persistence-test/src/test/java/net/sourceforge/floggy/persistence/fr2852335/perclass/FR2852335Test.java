/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.persistence.fr2852335.perclass;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import java.util.Calendar;
import java.util.Date;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreNotFoundException;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.fr2852335.perclass.AbstractSuperClass;
import net.sourceforge.floggy.persistence.fr2852335.perclass.CSCOfAbstractSuperClass;
import net.sourceforge.floggy.persistence.fr2852335.perclass.CSCOfConcreteSubClass;
import net.sourceforge.floggy.persistence.fr2852335.perclass.CSCOfConcreteSuperClass;
import net.sourceforge.floggy.persistence.fr2852335.perclass.ConcreteSuperClass;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.__Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2852335Test extends FloggyBaseTest {
	private static String name = "Floggy";
	private static Calendar checkpoint = Calendar.getInstance();
	private static char classification = 'F';
	private static Date start = new Date(987654258);

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFindConcreteSubClassOfConcreteSubClass()
		throws Exception {
		PersistableManager manager = PersistableManager.getInstance();

		CSCOfConcreteSubClass csc3 = new CSCOfConcreteSubClass();
		csc3.setName(name);
		csc3.setClassification(classification);
		csc3.setStart(start);

		try {
			manager.save(csc3);

			ObjectSet os = manager.find(CSCOfConcreteSubClass.class, null, null);
			assertEquals(1, os.size());
			assertEquals(CSCOfConcreteSubClass.class, os.get(0).getClass());
		} catch (Exception ex) {
			assertEquals(RecordStoreNotFoundException.class, ex.getClass());
		} finally {
			manager.delete(csc3);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFindConcreteSubClassOfConcreteSuperClass()
		throws Exception {
		PersistableManager manager = PersistableManager.getInstance();

		CSCOfConcreteSuperClass csc2 = new CSCOfConcreteSuperClass();
		csc2.setName(name);
		csc2.setClassification(classification);

		try {
			manager.save(csc2);

			ObjectSet os = manager.find(CSCOfConcreteSuperClass.class, null, null);
			assertEquals(1, os.size());
			assertEquals(CSCOfConcreteSuperClass.class, os.get(0).getClass());
		} catch (Exception ex) {
			assertEquals(RecordStoreNotFoundException.class, ex.getClass());
		} finally {
			manager.delete(csc2);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFindConcreteSuperClass() throws Exception {
		PersistableManager manager = PersistableManager.getInstance();

		ConcreteSuperClass csc1 = new ConcreteSuperClass();
		csc1.setName(name);

		CSCOfConcreteSuperClass csc2 = new CSCOfConcreteSuperClass();
		csc2.setName(name);
		csc2.setClassification(classification);

		CSCOfConcreteSubClass csc3 = new CSCOfConcreteSubClass();
		csc3.setName(name);
		csc3.setClassification(classification);
		csc3.setStart(start);

		try {
			manager.save(csc1);
			manager.save(csc2);
			manager.save(csc3);

			ObjectSet os = manager.find(ConcreteSuperClass.class, null, null);
			assertEquals(1, os.size());
			assertEquals(ConcreteSuperClass.class, os.get(0).getClass());
		} catch (Exception ex) {
			assertEquals(RecordStoreNotFoundException.class, ex.getClass());
		} finally {
			manager.delete(csc1);
			manager.delete(csc2);
			manager.delete(csc3);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFindMoreThanOneEntity() throws Exception {
		ConcreteSuperClass csc1 = new ConcreteSuperClass();
		csc1.setName(name);

		CSCOfConcreteSuperClass csc2 = new CSCOfConcreteSuperClass();
		csc2.setName(name);
		csc2.setClassification(classification);

		CSCOfConcreteSubClass csc3 = new CSCOfConcreteSubClass();
		csc3.setName(name);
		csc3.setClassification(classification);
		csc3.setStart(start);

		try {
			manager.save(csc1);
			manager.save(csc2);
			manager.save(csc3);

			ObjectSet os = manager.find(ConcreteSuperClass.class, null, null);
			assertEquals(1, os.size());
			assertEquals(ConcreteSuperClass.class, os.get(0).getClass());
		} finally {
			manager.delete(csc1);
			manager.delete(csc2);
			manager.delete(csc3);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasDeleteMethodInAbstractSuperClass()
		throws Exception {
		Method method = getMethod(AbstractSuperClass.class, "__delete");
		assertNotNull(method);
		assertTrue(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasDeleteMethodInConcreteSubClassOfConcreteSuperClass()
		throws Exception {
		Method method = getMethod(CSCOfConcreteSuperClass.class, "__delete");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasDeleteMethodInConcreteSuperClass()
		throws Exception {
		Method method = getMethod(ConcreteSuperClass.class, "__delete");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasDeserializeMethodInAbstractSuperClass()
		throws Exception {
		Method method = getMethod(AbstractSuperClass.class, "__deserialize");
		assertNotNull(method);
		assertTrue(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasDeserializeMethodInConcreteSubClassOfConcreteSuperClass()
		throws Exception {
		Method method = getMethod(CSCOfConcreteSuperClass.class, "__deserialize");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasDeserializeMethodInConcreteSuperClass()
		throws Exception {
		Method method = getMethod(ConcreteSuperClass.class, "__deserialize");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasGetIdMethodInAbstractSuperClass()
		throws Exception {
		Method method = getMethod(AbstractSuperClass.class, "__getId");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasGetIdMethodInConcreteSuperClass()
		throws Exception {
		Method method = getMethod(ConcreteSuperClass.class, "__getId");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasSerializeMethodInAbstractSuperClass()
		throws Exception {
		Method method = getMethod(AbstractSuperClass.class, "__serialize");
		assertNotNull(method);
		assertTrue(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasSerializeMethodInConcreteSubClassOfConcreteSuperClass()
		throws Exception {
		Method method = getMethod(CSCOfConcreteSuperClass.class, "__serialize");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasSerializeMethodInConcreteSuperClass()
		throws Exception {
		Method method = getMethod(ConcreteSuperClass.class, "__serialize");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasSetIdMethodInAbstractSuperClass()
		throws Exception {
		Method method = getMethod(AbstractSuperClass.class, "__setId");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHasSetIdMethodInConcreteSuperClass()
		throws Exception {
		Method method = getMethod(ConcreteSuperClass.class, "__setId");
		assertNotNull(method);
		assertFalse(Modifier.isAbstract(method.getModifiers()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testLoadWithAbstractSuperClass() throws Exception {
		CSCOfAbstractSuperClass csc = new CSCOfAbstractSuperClass();
		csc.setName(name);
		csc.setCheckpoint(checkpoint);

		try {
			int id = manager.save(csc);

			csc = new CSCOfAbstractSuperClass();

			manager.load(csc, id);

			assertEquals(name, csc.getName());
			assertEquals(checkpoint, csc.getCheckpoint());
		} finally {
			manager.delete(csc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testLoadWithConcreteSuperClass() throws Exception {
		PersistableManager manager = PersistableManager.getInstance();

		CSCOfConcreteSuperClass csc = new CSCOfConcreteSuperClass();
		csc.setName(name);
		csc.setClassification(classification);

		try {
			int id = manager.save(csc);

			csc = new CSCOfConcreteSuperClass();

			manager.load(csc, id);

			assertEquals(name, csc.getName());
			assertEquals(classification, csc.getClassification());
		} finally {
			manager.delete(csc);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testRecordStoreNameNotEquals() {
		PersistableMetadata m1 =
			PersistableMetadataManager.getClassBasedMetadata(ConcreteSuperClass.class
				 .getName());
		PersistableMetadata m2 =
			PersistableMetadataManager.getClassBasedMetadata(CSCOfConcreteSuperClass.class
				 .getName());
		PersistableMetadata m3 =
			PersistableMetadataManager.getClassBasedMetadata(CSCOfConcreteSubClass.class
				 .getName());

		assertFalse(m1.getRecordStoreName().equals(m2.getRecordStoreName()));
		assertFalse(m1.getRecordStoreName().equals(m3.getRecordStoreName()));
		assertFalse(m2.getRecordStoreName().equals(m3.getRecordStoreName()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testSaveWithAbstractSuperClass() throws Exception {
		CSCOfAbstractSuperClass csc = new CSCOfAbstractSuperClass();
		csc.setName(name);
		csc.setCheckpoint(checkpoint);

		try {
			manager.save(csc);

			PersistableMetadata metadata =
				PersistableMetadataManager.getClassBasedMetadata(CSCOfAbstractSuperClass.class
					 .getName());

			assertEquals(((__Persistable) csc).getRecordStoreName(),
				metadata.getRecordStoreName());

			RecordStore rs =
				RecordStore.openRecordStore(metadata.getRecordStoreName(), false);

			assertEquals(rs.getName(), metadata.getRecordStoreName());
		} catch (Exception ex) {
			fail(ex.getMessage());
		} finally {
			manager.delete(csc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testSaveWithConcreteSuperClass() throws Exception {
		PersistableManager manager = PersistableManager.getInstance();

		CSCOfConcreteSuperClass csc = new CSCOfConcreteSuperClass();
		csc.setName(name);
		csc.setClassification(classification);

		try {
			manager.save(csc);

			PersistableMetadata metadata =
				PersistableMetadataManager.getClassBasedMetadata(CSCOfConcreteSuperClass.class
					 .getName());

			assertEquals(((__Persistable) csc).getRecordStoreName(),
				metadata.getRecordStoreName());

			RecordStore rs =
				RecordStore.openRecordStore(metadata.getRecordStoreName(), false);

			assertEquals(rs.getName(), metadata.getRecordStoreName());
		} catch (Exception ex) {
			fail(ex.getMessage());
		} finally {
			manager.delete(csc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param clazz DOCUMENT ME!
	* @param methodName DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Method getMethod(Class clazz, String methodName) {
		Method[] methods = clazz.getMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals(methodName)) {
				return methods[i];
			}
		}

		return null;
	}
}
