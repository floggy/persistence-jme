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
package net.sourceforge.floggy.persistence.fr2852335.single;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

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
import net.sourceforge.floggy.persistence.impl.migration.JoinedStrategyEnumerationImpl;
import net.sourceforge.floggy.persistence.impl.migration.SingleStrategyEnumerationImpl;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2852335MigrationTest extends FloggyBaseTest {
	private static final Calendar checkpoint = Calendar.getInstance();
	private static final char classification = 'T';
	private static final String name = "Ubatuba";
	private static final Date birthDate = new Date(123456782);

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
	public void testFind() throws Exception {
		ConcreteSuperClass csc1 = new ConcreteSuperClass();
		csc1.setName(name);

		ConcreteSuperClass csc2 = new ConcreteSuperClass();
		csc2.setName(name);

		ConcreteSuperClass csc3 = new ConcreteSuperClass();
		csc3.setName(name);

		try {
			manager.save(csc1);
			manager.save(csc2);
			manager.save(csc3);

			ObjectSet os = manager.find(ConcreteSuperClass.class, null, null);
			assertEquals(4, os.size());
			assertEquals(ConcreteSuperClass.class, os.get(0).getClass());
		} finally {
			manager.delete(csc1);
			manager.delete(csc2);
			manager.delete(csc3);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testFindUpdatedToAbstractJoinedClass() {
		PersistableMetadata rmsBasedMetadata =
			PersistableMetadataManager.getRMSBasedMetadata(CSCOfAbstractJoinedStrategy.class
				 .getName());
		PersistableMetadata classBasedMetadata =
			PersistableMetadataManager.getClassBasedMetadata(CSCOfAbstractJoinedStrategy.class
				 .getName());

		assertEquals(PersistableMetadata.JOINED_STRATEGY,
			rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			classBasedMetadata.getPersistableStrategy());

		try {
			manager.find(CSCOfAbstractJoinedStrategy.class, null, null);
			fail(
				"Must throw a FloggyException because now the class implements the SingleStrategy startegy");
		} catch (Exception ex) {
			assertEquals(FloggyException.class, ex.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testFindUpdatedToConcreteJoinedClass() {
		PersistableMetadata rmsBasedMetadata =
			PersistableMetadataManager.getRMSBasedMetadata(CSCOfConcreteJoinedStrategy.class
				 .getName());
		PersistableMetadata classBasedMetadata =
			PersistableMetadataManager.getClassBasedMetadata(CSCOfConcreteJoinedStrategy.class
				 .getName());

		assertEquals(PersistableMetadata.JOINED_STRATEGY,
			rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			classBasedMetadata.getPersistableStrategy());

		try {
			manager.find(CSCOfConcreteJoinedStrategy.class, null, null);
			fail(
				"Must throw a FloggyException because now the class implements the SingleStrategy startegy");
		} catch (Exception ex) {
			assertEquals(FloggyException.class, ex.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testMigrationCSCOfConcreteSubClass() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(CSCOfConcreteSubClass.class, null);

		assertEquals(1, enumeration.getSize());
		assertEquals(SingleStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable fields = enumeration.nextElement();
				assertEquals(classification,
					((Character) fields.get("classification")).charValue());
				assertEquals(name, fields.get("name"));
				assertEquals(checkpoint.getTime(), fields.get("start"));
			}
		} finally {
			um.finish(CSCOfConcreteSubClass.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testMigrationCSCOfConcreteSuperClass() throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(CSCOfConcreteSuperClass.class, null);

		assertEquals(1, enumeration.getSize());
		assertEquals(SingleStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable fields = enumeration.nextElement();
				assertEquals(classification,
					((Character) fields.get("classification")).charValue());
				assertEquals(name, fields.get("name"));
			}
		} finally {
			um.finish(CSCOfConcreteSuperClass.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testMigrationConcreteSubClassOfAbstractSuperClass()
		throws Exception {
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(CSCOfAbstractSuperClass.class, null);

		assertEquals(1, enumeration.getSize());
		assertEquals(SingleStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable fields = enumeration.nextElement();
				assertEquals(checkpoint, fields.get("checkpoint"));
				assertEquals(name, fields.get("name"));
			}
		} finally {
			um.finish(CSCOfAbstractSuperClass.class);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testMigrationOfUpdatedToAbstractJoinedClass()
		throws Exception {
		PersistableMetadata rmsBasedMetadata =
			PersistableMetadataManager.getRMSBasedMetadata(CSCOfAbstractJoinedStrategy.class
				 .getName());
		PersistableMetadata classBasedMetadata =
			PersistableMetadataManager.getClassBasedMetadata(CSCOfAbstractJoinedStrategy.class
				 .getName());

		assertEquals(PersistableMetadata.JOINED_STRATEGY,
			rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			classBasedMetadata.getPersistableStrategy());

		MigrationManager mm = MigrationManager.getInstance();
		Enumeration enumeration = mm.start(CSCOfAbstractJoinedStrategy.class, null);

		assertEquals(JoinedStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			assertEquals(1, enumeration.getSize());

			while (enumeration.hasMoreElements()) {
				Hashtable data = enumeration.nextElement();
				assertEquals(name, data.get("name"));
				assertEquals(birthDate, data.get("birthDate"));
			}
		} finally {
			mm.finish(CSCOfAbstractJoinedStrategy.class);
		}

		rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(CSCOfAbstractJoinedStrategy.class
				 .getName());
		classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(CSCOfAbstractJoinedStrategy.class
				 .getName());

		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			classBasedMetadata.getPersistableStrategy());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testMigrationOfUpdatedToConcreteJoinedClass()
		throws Exception {
		PersistableMetadata rmsBasedMetadata =
			PersistableMetadataManager.getRMSBasedMetadata(CSCOfConcreteJoinedStrategy.class
				 .getName());
		PersistableMetadata classBasedMetadata =
			PersistableMetadataManager.getClassBasedMetadata(CSCOfConcreteJoinedStrategy.class
				 .getName());

		assertEquals(PersistableMetadata.JOINED_STRATEGY,
			rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			classBasedMetadata.getPersistableStrategy());

		MigrationManager mm = MigrationManager.getInstance();
		Enumeration enumeration = mm.start(CSCOfConcreteJoinedStrategy.class, null);

		assertEquals(JoinedStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			assertEquals(1, enumeration.getSize());

			while (enumeration.hasMoreElements()) {
				Hashtable data = enumeration.nextElement();
				assertEquals(name, data.get("name"));
				assertEquals(birthDate, data.get("birthDate"));
			}
		} finally {
			mm.finish(CSCOfConcreteJoinedStrategy.class);
		}

		rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(CSCOfConcreteJoinedStrategy.class
				 .getName());
		classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(CSCOfConcreteJoinedStrategy.class
				 .getName());

		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.SINGLE_STRATEGY,
			classBasedMetadata.getPersistableStrategy());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testMigrationUpdateConcreteSuperClass() throws Exception {
		ConcreteSuperClass csc = new ConcreteSuperClass();
		csc.setName(name);

		manager.save(csc);

		ConcreteSuperClass csc2 = new ConcreteSuperClass();
		csc2.setName(name);

		manager.save(csc2);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(ConcreteSuperClass.class, null);

		assertEquals(3, enumeration.getSize());
		assertEquals(SingleStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable fields = enumeration.nextElement();
				assertEquals(name, fields.get("name"));

				csc = new ConcreteSuperClass();
				csc.setName((String) fields.get("name"));

				enumeration.update(csc);
			}
		} finally {
			um.finish(ConcreteSuperClass.class);
		}

		ObjectSet os = manager.find(ConcreteSuperClass.class, null, null);

		assertEquals(3, os.size());

		for (int i = 0; i < os.size(); i++) {
			csc = (ConcreteSuperClass) os.get(i);
			assertEquals(name, csc.getName());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected void setUp() throws Exception {
		emulator = MIDletBridge.getMicroEmulator();
		FileUtils.forceMkdir(new File("target/fr2852335/rms/1.4.0/single"));
		IOUtils.copy(new FileInputStream(
				"src/test/rms/1.4.0/single/FloggyProperties.rms"),
			new FileOutputStream(
				"target/fr2852335/rms/1.4.0/single/FloggyProperties.rms"));
		IOUtils.copy(new FileInputStream(
				"src/test/rms/1.4.0/single/AbstractSuperClass-1688916957.rms"),
			new FileOutputStream(
				"target/fr2852335/rms/1.4.0/single/AbstractSuperClass-1688916957.rms"));
		IOUtils.copy(new FileInputStream(
				"src/test/rms/1.4.0/single/ConcreteSuperClass1629449926.rms"),
			new FileOutputStream(
				"target/fr2852335/rms/1.4.0/single/ConcreteSuperClass1629449926.rms"));
		IOUtils.copy(new FileInputStream(
				"src/test/rms/1.4.0/single/AbstractJoinedStrategy1339113538.rms"),
			new FileOutputStream(
				"target/fr2852335/rms/1.4.0/single/AbstractJoinedStrategy1339113538.rms"));
		IOUtils.copy(new FileInputStream(
				"src/test/rms/1.4.0/single/CSCOfAbstractJoinedStrategy-2212.rms"),
			new FileOutputStream(
				"target/fr2852335/rms/1.4.0/single/CSCOfAbstractJoinedStrategy-2212.rms"));
		IOUtils.copy(new FileInputStream(
				"src/test/rms/1.4.0/single/ConcreteJoinedStrategy-878479003.rms"),
			new FileOutputStream(
				"target/fr2852335/rms/1.4.0/single/ConcreteJoinedStrategy-878479003.rms"));
		IOUtils.copy(new FileInputStream(
				"src/test/rms/1.4.0/single/CSCOfConcreteJoinedStrategy18561.rms"),
			new FileOutputStream(
				"target/fr2852335/rms/1.4.0/single/CSCOfConcreteJoinedStrategy18561.rms"));
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator(
				"target/fr2852335/rms/1.4.0/single"));
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
