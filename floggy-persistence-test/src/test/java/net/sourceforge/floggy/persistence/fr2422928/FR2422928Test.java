/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.persistence.fr2422928;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import javax.microedition.rms.RecordStore;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.microemu.MIDletBridge;
import org.microemu.MicroEmulator;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.impl.MetadataManagerUtil;
import net.sourceforge.floggy.persistence.impl.PersistableManagerImpl;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2422928Test extends FloggyBaseTest {
	private static final long id = 652345;
	private static final String name = "FR2422928";
	private static final Calendar checkpoint = Calendar.getInstance();

	static {
		TimeZone zone = TimeZone.getTimeZone("America/Sao_Paulo");
		checkpoint.setTimeZone(zone);
		checkpoint.setTimeInMillis(1234567890);
	}

	/**
	 * DOCUMENT ME!
	 */
	protected MicroEmulator emulator;

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAbstractInheritance() throws Exception {
		Date creationDate = new Date(123456789);

		AbstractSuperClass asc = new ConcreteChildClass();
		asc.setCreationDate(creationDate);

		manager.save(asc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(ConcreteChildClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertEquals(creationDate, data.get("creationDate"));
				assertEquals(new Vector(), data.get("dynamicFields"));
			}
		} finally {
			um.finish(ConcreteChildClass.class);
			manager.delete(asc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAbstractInheritanceDelete() throws Exception {
		AbstractSuperClass asc = new ConcreteChildClass();
		asc.setCreationDate(new Date());

		manager.save(asc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(ConcreteChildClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				enumeration.nextElement();
				enumeration.delete();
			}

			ObjectSet os = manager.find(ConcreteChildClass.class, null, null);
			assertEquals(0, os.size());

			PersistableMetadata metadata =
				MetadataManagerUtil.getClassBasedMetadata(AbstractSuperClass.class
					 .getName());
			RecordStore rs =
				PersistableManagerImpl.getRecordStore(metadata.getRecordStoreName(),
					metadata);

			assertEquals(0, rs.getNumRecords());
		} finally {
			um.finish(ConcreteChildClass.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAbstractInheritanceUpdate() throws Exception {
		AbstractSuperClass asc = new ConcreteChildClass();
		asc.setCreationDate(new Date());

		int id = manager.save(asc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(ConcreteChildClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				enumeration.nextElement();
				asc = new ConcreteChildClass();

				int tempId = enumeration.update(asc);
				assertEquals(id, tempId);
			}

			ObjectSet os = manager.find(ConcreteChildClass.class, null, null);
			assertEquals(1, os.size());

			PersistableMetadata metadata =
				MetadataManagerUtil.getClassBasedMetadata(AbstractSuperClass.class
					 .getName());
			RecordStore rs =
				PersistableManagerImpl.getRecordStore(metadata.getRecordStoreName(),
					metadata);

			assertEquals(1, rs.getNumRecords());
		} finally {
			um.finish(ConcreteChildClass.class);
			manager.delete(asc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAfterUpddate() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(FR2422928.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertFalse("Should not be empty!", data.isEmpty());
				assertEquals(name, data.get("name"));
				assertEquals(checkpoint, data.get("checkpoint"));

				FR2422928 persistable = new FR2422928();
				persistable.setName((String) data.get("name"));
				persistable.setCheckpoint((Calendar) data.get("checkpoint"));
				enumeration.update(persistable);
			}
		} finally {
			um.finish(FR2422928.class);
		}

		try {
			ObjectSet os = manager.find(FR2422928.class, null, null);
			assertEquals(1, os.size());

			FR2422928 persistable = (FR2422928) os.get(0);
			assertEquals(name, persistable.getName());
			assertEquals(checkpoint, persistable.getCheckpoint());
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testBeforeUpddate() throws Exception {
		try {
			ObjectSet os = manager.find(FR2422928.class, null, null);
			os.get(0);
			fail(
				"Should throw a exception because the rms layout is different from the class fields!");
		} catch (Exception ex) {
			assertTrue(true);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testDoubleCalltoNextElement() throws Exception {
		Bird bird1 = new Bird();
		bird1.setColor("green");

		Bird bird2 = new Bird();
		bird2.setColor("yellow");

		manager.save(bird1);
		manager.save(bird2);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(Bird.class, null);

		try {
			enumeration.nextElement();
			enumeration.nextElement();
			fail("Must throw a FloggyException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		} finally {
			manager.delete(bird1);
			manager.delete(bird2);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetNotMigratedClasses() {
		MigrationManager um = MigrationManager.getInstance();
		assertNotNull(um.getNotMigratedClasses());
		assertTrue(um.getNotMigratedClasses().length > 0);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testGetSize() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(FR2422928.class, null);
		assertEquals(1, enumeration.getSize());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testInheritanceDelete() throws Exception {
		String name = "Floggy";
		int age = 23;

		ChildClass cc = new ChildClass();
		cc.setAge(age);
		cc.setName(name);

		manager.save(cc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(ChildClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				enumeration.nextElement();
				enumeration.delete();
			}

			ObjectSet os = manager.find(ChildClass.class, null, null);
			assertEquals(0, os.size());

			os = manager.find(SuperClass.class, null, null);
			assertEquals(0, os.size());
		} finally {
			um.finish(ChildClass.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testInheritanceRead() throws Exception {
		String name = "Floggy";
		int age = 23;

		ChildClass cc = new ChildClass();
		cc.setAge(age);
		cc.setName(name);

		manager.save(cc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(ChildClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertEquals(name, data.get("name"));
				assertEquals(new Integer(age), data.get("age"));
			}
		} finally {
			um.finish(ChildClass.class);
			manager.delete(cc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testInheritanceUpdate() throws Exception {
		String name = "Floggy";
		int age = 23;

		ChildClass cc = new ChildClass();
		cc.setAge(age);
		cc.setName(name);

		int id = manager.save(cc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(ChildClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				enumeration.nextElement();
				cc = new ChildClass();

				int tempId = enumeration.update(cc);
				assertEquals(id, tempId);
			}

			ObjectSet os = manager.find(ChildClass.class, null, null);
			assertEquals(1, os.size());

			os = manager.find(SuperClass.class, null, null);
			assertEquals(1, os.size());
		} finally {
			um.finish(ChildClass.class);
			manager.delete(cc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testIsEnumerationAtTheEndWhenFinishing()
		throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		um.start(FR2422928.class, null);

		try {
			um.finish(FR2422928.class);
			fail("Must throw a exception because no iteration was made!");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testIterationMode() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Hashtable properties = new Hashtable();
		Freezed freezed = new Freezed();

		freezed.setNested(new Freezed());

		try {
			manager.save(freezed);

			properties.put(MigrationManager.ITERATION_MODE, Boolean.TRUE);

			Enumeration enumeration = um.start(Freezed.class, properties);

			assertEquals(2, enumeration.getSize());

			while (enumeration.hasMoreElements()) {
				assertNotNull(enumeration.nextElement());
			}
		} finally {
			manager.delete(freezed);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testLazyTrue() throws Exception {
		String color = "white";

		Bird falcon = new Falcon();
		falcon.setColor(color);

		Person person = new Person();
		person.setX(falcon);

		manager.save(person);

		MigrationManager um = MigrationManager.getInstance();

		Hashtable properties = new Hashtable();
		properties.put(MigrationManager.LAZY_LOAD, Boolean.TRUE);

		Enumeration enumeration = um.start(Person.class, properties);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertNull(data.get("x"));
			}
		} finally {
			um.finish(Person.class);
			manager.delete(person);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testMissingField() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(FR2422928.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertFalse("Should not be empty!", data.isEmpty());
				assertEquals(new Long(id), (Long) data.get("id"));
			}
		} finally {
			um.finish(FR2422928.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testNewField() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(FR2422928.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertFalse("Should not be empty!", data.isEmpty());
				assertNull(data.get("node"));
			}
		} finally {
			um.finish(FR2422928.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testPersistableFielded() throws Exception {
		String color = "blue";
		Date now = new Date();

		Bird bird = new Bird();
		bird.setColor(color);

		PersistableFieldedClass pfc = new PersistableFieldedClass();
		pfc.setBird(bird);
		pfc.setCreationDate(now);

		manager.save(pfc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(PersistableFieldedClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				FieldPersistableInfo field = (FieldPersistableInfo) data.get("bird");
				String className = field.getClassName();

				Bird b = (Bird) Class.forName(className).newInstance();

				manager.load(b, field.getId());
				assertEquals(color, b.getColor());
			}
		} finally {
			um.finish(PersistableFieldedClass.class);
			manager.delete(pfc);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testPersistableSubclassedField() throws Exception {
		String color = "blue";
		Date now = new Date();

		Bird bird = new Falcon();
		bird.setColor(color);

		PersistableFieldedClass pfc = new PersistableFieldedClass();
		pfc.setBird(bird);
		pfc.setCreationDate(now);

		manager.save(pfc);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(PersistableFieldedClass.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				FieldPersistableInfo field = (FieldPersistableInfo) data.get("bird");

				Bird b = (Bird) Class.forName(field.getClassName()).newInstance();
				manager.load(b, field.getId());
				assertEquals(color, b.getColor());
				enumeration.delete();
			}
		} finally {
			um.finish(PersistableFieldedClass.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testQuickMigrationThrowingException() {
		MigrationManager um = MigrationManager.getInstance();

		try {
			um.quickMigration(FR2422928.class);
			fail(
				"It must throw a FloggyException because the class has different metadatas for RMS and Bytecode");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testRemaingField() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(FR2422928.class, null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertFalse("Should not be empty!", data.isEmpty());
				assertEquals(name, (String) data.get("name"));
				assertEquals(checkpoint, data.get("checkpoint"));
			}
		} finally {
			um.finish(FR2422928.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionFinishWithNullParameter() {
		try {
			MigrationManager.getInstance().finish(null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), IllegalArgumentException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionFinishWithStringClassParameter() {
		try {
			MigrationManager.getInstance().finish(String.class);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), IllegalArgumentException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionQuickMigrationWithNullParameter() {
		try {
			MigrationManager.getInstance().quickMigration(null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), IllegalArgumentException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionQuickMigrationWithStringClassParameter() {
		try {
			MigrationManager.getInstance().quickMigration(String.class);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), IllegalArgumentException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionStartWithNullParameter() {
		try {
			MigrationManager.getInstance().start(null, null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), IllegalArgumentException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionStartWithStringClassParameter() {
		try {
			MigrationManager.getInstance().start(String.class, null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), IllegalArgumentException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionWhenFieldNotMigratedWithLazyFalse() {
		MigrationManager um = MigrationManager.getInstance();

		try {
			um.start(FR2422928Holder.class, null);
			fail("It must throw a FloggyException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionWhenFieldNotMigratedWithLazyTrue() {
		MigrationManager um = MigrationManager.getInstance();

		try {
			Hashtable properties = new Hashtable();
			properties.put(MigrationManager.LAZY_LOAD, Boolean.TRUE);
			properties.put(MigrationManager.MIGRATE_FROM_PREVIOUS_1_3_0_VERSION,
				Boolean.TRUE);

			Enumeration enumeration = um.start(FR2422928Holder.class, properties);
			assertNotNull(enumeration);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionWhenMigratingAbstractClass() {
		MigrationManager um = MigrationManager.getInstance();

		try {
			um.start(AbstractSuperClass.class, null);
			fail("It must throw a FloggyException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionWhenNotMigrated() {
		try {
			manager.save(new FR2422928());
			fail("It must throw a FloggyException");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected void setUp() throws Exception {
		emulator = MIDletBridge.getMicroEmulator();
		FileUtils.forceMkdir(new File("target/fr2422928/rms/1.3.0"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.3.0/FloggyProperties.rms"),
			new FileOutputStream("target/fr2422928/rms/1.3.0/FloggyProperties.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.3.0/FR2422928.rms"),
			new FileOutputStream("target/fr2422928/rms/1.3.0/FR2422928.rms"));
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator(
				"target/fr2422928/rms/1.3.0"));
		MetadataManagerUtil.init();
		PersistableManagerImpl.reset();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected void tearDown() throws Exception {
		MIDletBridge.setMicroEmulator(emulator);
		MetadataManagerUtil.init();
	}
}
