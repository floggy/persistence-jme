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

import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.microemu.MIDletBridge;
import org.microemu.MicroEmulator;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;
import net.sourceforge.floggy.persistence.impl.MetadataManagerUtil;
import net.sourceforge.floggy.persistence.impl.PersistableManagerImpl;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class FR2422928AbstractVersionTest extends FloggyBaseTest {
	/**
	 * DOCUMENT ME!
	 */
	protected MicroEmulator emulator;

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public abstract String getVersion();

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFreezedClassIteration() throws Exception {
		Hashtable properties = new Hashtable();
		properties.put(MigrationManager.MIGRATE_FROM_PREVIOUS_1_3_0_VERSION,
			Boolean.TRUE);
		properties.put(MigrationManager.ITERATION_MODE, Boolean.TRUE);

		Enumeration enumeration =
			MigrationManager.getInstance().start(Freezed.class, properties);

		assertEquals(2, enumeration.getSize());

		if (enumeration.hasMoreElements()) {
			Hashtable data = enumeration.nextElement();
			assertNull(data.get("uuid"));
			assertNull(data.get("deadline"));
			assertNull(data.get("description"));
			assertNull(data.get("nested"));
			assertEquals(0, ((Short) data.get("code")).shortValue());
		}

		if (enumeration.hasMoreElements()) {
			Hashtable data = enumeration.nextElement();
			assertEquals(Freezed.UUID, data.get("uuid"));
			assertEquals(Freezed.DEADLINE, data.get("deadline"));
			assertEquals(Freezed.DESCRIPTION, data.get("description"));

			FieldPersistableInfo freezedInfo =
				(FieldPersistableInfo) data.get("nested");
			Freezed nested = new Freezed();
			manager.load(nested, freezedInfo.getId());

			assertEquals(Freezed.NESTED, nested);
			assertEquals(Freezed.CODE, ((Short) data.get("code")).shortValue());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetRMSBasedMetadata() {
		assertNull(MetadataManagerUtil.getRMSBasedMetadata(
				FR2422928.class.getName()));
		assertNull(MetadataManagerUtil.getRMSBasedMetadata(Freezed.class.getName()));
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetRMSVersion() {
		assertEquals(getVersion(), MetadataManagerUtil.getRMSVersion());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testNoMigrationExecution() {
		try {
			manager.find(Freezed.class, null, null);
			fail("Should throw a exception because no migration was made!");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testNotThrowExceptionWhenMigratingFromPreviousVersion() {
		Hashtable properties = new Hashtable();
		properties.put(MigrationManager.MIGRATE_FROM_PREVIOUS_1_3_0_VERSION,
			Boolean.TRUE);

		try {
			Enumeration enumeration =
				MigrationManager.getInstance().start(FR2422928.class, properties);
			assertNotNull(enumeration);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testQuickMigration() {
		MigrationManager manager = MigrationManager.getInstance();

		List notMigratedClasses = Arrays.asList(manager.getNotMigratedClasses());
		assertTrue(notMigratedClasses.contains(Freezed.class.getName()));

		try {
			manager.quickMigration(Freezed.class);

			notMigratedClasses = Arrays.asList(manager.getNotMigratedClasses());
			assertFalse(notMigratedClasses.contains(Freezed.class.getName()));
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testQuickMigrationOfAll() throws Exception {
		MigrationManager manager = MigrationManager.getInstance();
		String[] notMigratedClasses = manager.getNotMigratedClasses();

		for (int i = 0; i < notMigratedClasses.length; i++) {
			String className = notMigratedClasses[i];
			manager.quickMigration(Class.forName(className));
		}

		notMigratedClasses = manager.getNotMigratedClasses();
		assertNotNull(notMigratedClasses);
		assertEquals(0, notMigratedClasses.length);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testThrowExceptionWhenMigratingFromPreviousVersion() {
		try {
			MigrationManager.getInstance().start(FR2422928.class, null);
			fail(
				"Should throw a exception because the property MigrationManager.MIGRATE_FROM_PREVIOUS_FLOGGY_VERSION is not set to true!");
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
		FileUtils.forceMkdir(new File("target/fr2422928/rms/" + getVersion()));
		IOUtils.copy(new FileInputStream("src/test/rms/" + getVersion()
				+ "/FloggyProperties.rms"),
			new FileOutputStream("target/fr2422928/rms/" + getVersion()
				+ "/FloggyProperties.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/" + getVersion()
				+ "/FR2422928-284317062.rms"),
			new FileOutputStream("target/fr2422928/rms/" + getVersion()
				+ "/FR2422928-284317062.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/" + getVersion()
				+ "/Freezed-1739440490.rms"),
			new FileOutputStream("target/fr2422928/rms/" + getVersion()
				+ "/Freezed-1739440490.rms"));
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator(
				"target/fr2422928/rms/" + getVersion()));
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
