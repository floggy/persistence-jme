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
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2422928MigrationTest extends FloggyBaseTest {
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
	public void testAfterUpddate() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(FR2422928.class, null);

		PersistableMetadata metadata =
			PersistableMetadataManager.getRMSBasedMetadata(FR2422928.class.getName());

		assertEquals(PersistableMetadataManager.VERSION_1_3_0,
			metadata.getRecordStoreVersion());

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

		metadata = PersistableMetadataManager.getRMSBasedMetadata(FR2422928.class
				 .getName());

		assertEquals(PersistableMetadataManager.getBytecodeVersion(),
			metadata.getRecordStoreVersion());

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
			assertEquals(0, os.size());
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
		PersistableMetadataManager.init();
		RecordStoreManager.reset();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected void tearDown() throws Exception {
		MIDletBridge.setMicroEmulator(emulator);
		PersistableMetadataManager.init();
	}
}
