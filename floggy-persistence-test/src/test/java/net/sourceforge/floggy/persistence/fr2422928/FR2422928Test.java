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

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

import javax.microedition.rms.RecordStore;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

import org.microemu.MicroEmulator;

public class FR2422928Test extends FloggyBaseTest {

	private static final Calendar checkpoint = Calendar.getInstance();  
	protected MicroEmulator emulator;
	
	static {
		TimeZone zone = TimeZone.getTimeZone("America/Sao_Paulo");
		checkpoint.setTimeZone(zone);
		checkpoint.setTimeInMillis(1234567890);
	}
	
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
				Hashtable data = (Hashtable)enumeration.nextElement();
				assertNull(data.get("x"));
			}
		} finally {
			um.finish(Person.class);
			manager.delete(person);
		}
	}

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
				Hashtable data = (Hashtable)enumeration.nextElement();
				FieldPersistableInfo field = (FieldPersistableInfo) data.get("bird");
				String className = field.getClassName();

				Bird b = (Bird)Class.forName(className).newInstance();

				manager.load(b, field.getId());
				assertEquals(color, b.getColor());
			}
		} finally {
			um.finish(PersistableFieldedClass.class);
			manager.delete(pfc);
		}
	}

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
				Hashtable data = (Hashtable)enumeration.nextElement();
				FieldPersistableInfo field = (FieldPersistableInfo) data.get("bird");

				Bird b = (Bird)Class.forName(field.getClassName()).newInstance();
				manager.load(b, field.getId());
				assertEquals(color, b.getColor());
				enumeration.delete();
			}
		} finally {
			um.finish(PersistableFieldedClass.class);
		}
	}
	
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

			PersistableMetadata metadata = PersistableMetadataManager.getClassBasedMetadata(AbstractSuperClass.class.getName());
			RecordStore rs = RecordStoreManager.getRecordStore(metadata.getRecordStoreName(), metadata);
			
			assertEquals(0, rs.getNumRecords());
		} finally {
			um.finish(ConcreteChildClass.class);
		}
	}

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

			PersistableMetadata metadata = PersistableMetadataManager.getClassBasedMetadata(AbstractSuperClass.class.getName());
			RecordStore rs = RecordStoreManager.getRecordStore(metadata.getRecordStoreName(), metadata);
			
			assertEquals(1, rs.getNumRecords());
		} finally {
			um.finish(ConcreteChildClass.class);
			manager.delete(asc);
		}
	}

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



}
