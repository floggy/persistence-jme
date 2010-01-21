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

package net.sourceforge.floggy.persistence.fr2852335.perclass;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;
import net.sourceforge.floggy.persistence.impl.migration.JoinedStrategyEnumerationImpl;
import net.sourceforge.floggy.persistence.impl.migration.PerClassStrategyEnumerationImpl;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.microemu.MIDletBridge;
import org.microemu.MicroEmulator;

public class FR2852335MigrationTest extends FloggyBaseTest {
	
	private static final Calendar checkpoint = Calendar.getInstance();
	private static final char classification = 'T';
	private static final String name = "Ubatuba";
	private static final Date birthDate = new Date(123456782);

	protected MicroEmulator emulator;
	
	static {
		TimeZone zone = TimeZone.getTimeZone("America/Sao_Paulo");
		checkpoint.setTimeZone(zone);
		checkpoint.setTimeInMillis(1234567890);
	}

	protected void setUp() throws Exception {
		emulator = MIDletBridge.getMicroEmulator();
		FileUtils.forceMkdir(new File("target/fr2852335/rms/1.4.0/perclass"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/FloggyProperties.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/FloggyProperties.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/CSCOfAbstractSuperClass-11335278.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/CSCOfAbstractSuperClass-11335278.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/CSCOfConcreteSuperClass-21101282.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/CSCOfConcreteSuperClass-21101282.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/CSCOfConcreteSubClass1826896726.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/CSCOfConcreteSubClass1826896726.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/ConcreteSuperClass-766888397.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/ConcreteSuperClass-766888397.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/AbstractJoinedStrategy373328175.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/AbstractJoinedStrategy373328175.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/CSCOfAbstractJoinedStrategy12600.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/CSCOfAbstractJoinedStrategy12600.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/ConcreteJoinedStrategy-184426436.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/ConcreteJoinedStrategy-184426436.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/perclass/CSCOfConcreteJoinedStrategy-9575.rms"), new FileOutputStream("target/fr2852335/rms/1.4.0/perclass/CSCOfConcreteJoinedStrategy-9575.rms"));
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator("target/fr2852335/rms/1.4.0/perclass"));
		PersistableMetadataManager.init();
		RecordStoreManager.reset();
	}
	
	protected void tearDown() throws Exception {
		MIDletBridge.setMicroEmulator(emulator);
		PersistableMetadataManager.init();
	}

//	public void testGenerateRMS() throws Exception {
//		//ConcreteSubClassOfAbstractSuperClass
//		CSCOfAbstractSuperClass cscoasc = new CSCOfAbstractSuperClass();
//		cscoasc.setCheckpoint(checkpoint);
//		cscoasc.setName(name);
//		
//		manager.save(cscoasc);
//		
//		//ConcreteSuperClass
//		ConcreteSuperClass csc = new ConcreteSuperClass();
//		csc.setName(name);
//		
//		manager.save(csc);
//	
//		//ConcreteSubClassOfConcreteSuperClass
//		CSCOfConcreteSuperClass cscocsuper = new CSCOfConcreteSuperClass();
//		cscocsuper.setClassification(classification);
//		cscocsuper.setName(name);
//		
//		manager.save(cscocsuper);
//	
//		//ConcreteSubClassOfConcreteSubClass
//		CSCOfConcreteSubClass cscocsub = new CSCOfConcreteSubClass();
//		cscocsub.setClassification(classification);
//		cscocsub.setName(name);
//		cscocsub.setStart(checkpoint.getTime());
//		
//		manager.save(cscocsub);
//		
//		CSCOfAbstractJoinedStrategy cscoajs = new CSCOfAbstractJoinedStrategy();
//		cscoajs.setName(name);
//		cscoajs.setBirthDate(birthDate);
//		
//		manager.save(cscoajs);
//		
//		CSCOfConcreteJoinedStrategy cscocjs = new CSCOfConcreteJoinedStrategy();
//		cscocjs.setName(name);
//		cscocjs.setBirthDate(birthDate);
//		
//		manager.save(cscocjs);
//	}

	public void testMigrationConcreteSubClassOfAbstractSuperClass() throws Exception {
		MigrationManager mm = MigrationManager.getInstance();
		Enumeration enumeration = mm.start(CSCOfAbstractSuperClass.class, null);
		
		assertEquals(PerClassStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable fields = enumeration.nextElement();
				assertEquals(checkpoint, fields.get("checkpoint"));
				assertEquals(name, fields.get("name"));
			}
		} finally {
			mm.finish(CSCOfAbstractSuperClass.class);
		}
	}

	public void testMigrationConcreteSubClassOfConcreteSuperClass() throws Exception {
		MigrationManager mm = MigrationManager.getInstance();
		Enumeration enumeration = mm.start(CSCOfConcreteSuperClass.class, null);
		
		assertEquals(PerClassStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable fields = enumeration.nextElement();
				assertEquals(classification, ((Character)fields.get("classification")).charValue());
				assertEquals(name, fields.get("name"));
			}
		} finally {
			mm.finish(CSCOfConcreteSuperClass.class);
		}
	}

	public void testMigrationConcreteSubClassOfConcreteSubClass() throws Exception {
		MigrationManager mm = MigrationManager.getInstance();
		Enumeration enumeration = mm.start(CSCOfConcreteSubClass.class, null);
		
		assertEquals(PerClassStrategyEnumerationImpl.class, enumeration.getClass());

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable fields = enumeration.nextElement();
				assertEquals(classification, ((Character)fields.get("classification")).charValue());
				assertEquals(name, fields.get("name"));
				assertEquals(checkpoint.getTime(), fields.get("start"));
			}
		} finally {
			mm.finish(CSCOfConcreteSubClass.class);
		}
	}
	
	public void testFindUpdatedToAbstractJoinedClass() {
		PersistableMetadata rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(CSCOfAbstractJoinedStrategy.class.getName());
		PersistableMetadata classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(CSCOfAbstractJoinedStrategy.class.getName());
		
		assertEquals(PersistableMetadata.JOINED_STRATEGY, rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.PER_CLASS_STRATEGY, classBasedMetadata.getPersistableStrategy());
		
		try {
			manager.find(CSCOfAbstractJoinedStrategy.class, null, null);
			fail("Must throw a FloggyException because now the class implements the PerClassStrategy startegy");
		} catch (Exception ex) {
			assertEquals(FloggyException.class, ex.getClass());
		}
	}

	public void testMIgrationOfUpdatedToAbstractJoinedClass() throws Exception {
		MigrationManager mm = MigrationManager.getInstance();
		Enumeration enumeration = mm.start(CSCOfAbstractJoinedStrategy.class, null);
		
		assertEquals(JoinedStrategyEnumerationImpl.class, enumeration.getClass());
		
		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = enumeration.nextElement();
				assertEquals(name, data.get("name"));
				assertEquals(birthDate, data.get("birthDate"));
			}
		} finally {
			mm.finish(CSCOfAbstractJoinedStrategy.class);
		}

		PersistableMetadata rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(CSCOfAbstractJoinedStrategy.class.getName());
		PersistableMetadata classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(CSCOfAbstractJoinedStrategy.class.getName());

		assertEquals(PersistableMetadata.PER_CLASS_STRATEGY, rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.PER_CLASS_STRATEGY, classBasedMetadata.getPersistableStrategy());
	}

	public void testFindUpdatedToConcreteJoinedClass() {
		PersistableMetadata rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(CSCOfConcreteJoinedStrategy.class.getName());
		PersistableMetadata classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(CSCOfConcreteJoinedStrategy.class.getName());
		
		assertEquals(PersistableMetadata.JOINED_STRATEGY, rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.PER_CLASS_STRATEGY, classBasedMetadata.getPersistableStrategy());
		
		try {
			manager.find(CSCOfConcreteJoinedStrategy.class, null, null);
			fail("Must throw a FloggyException because now the class implements the PerClassStrategy startegy");
		} catch (Exception ex) {
			assertEquals(FloggyException.class, ex.getClass());
		}
	}

	public void testMIgrationOfUpdatedToConcreteJoinedClass() throws Exception {
		MigrationManager mm = MigrationManager.getInstance();
		Enumeration enumeration = mm.start(CSCOfConcreteJoinedStrategy.class, null);
		
		assertEquals(JoinedStrategyEnumerationImpl.class, enumeration.getClass());
		
		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = enumeration.nextElement();
				assertEquals(name, data.get("name"));
				assertEquals(birthDate, data.get("birthDate"));
			}
		} finally {
			mm.finish(CSCOfConcreteJoinedStrategy.class);
		}

		PersistableMetadata rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(CSCOfConcreteJoinedStrategy.class.getName());
		PersistableMetadata classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(CSCOfAbstractJoinedStrategy.class.getName());

		assertEquals(PersistableMetadata.PER_CLASS_STRATEGY, rmsBasedMetadata.getPersistableStrategy());
		assertEquals(PersistableMetadata.PER_CLASS_STRATEGY, classBasedMetadata.getPersistableStrategy());
	}

}
