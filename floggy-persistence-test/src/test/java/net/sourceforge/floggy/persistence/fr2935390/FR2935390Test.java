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

package net.sourceforge.floggy.persistence.fr2935390;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.Vector;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.IndexFilter;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.impl.IndexManager;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.microemu.MIDletBridge;
import org.microemu.MicroEmulator;

public class FR2935390Test extends FloggyBaseTest {

	protected Vector buildOrderedPersons(int quantity) {
		Vector vector = new Vector(quantity);
		for (int i = 0; i < quantity; i++) {
			Bird bird = new Bird();
			String color = "color" + i;
			bird.setColor(color);

			vector.addElement(bird);
		}

		return vector;
	}

	public void testDoesNotExistIndexName() throws Exception {
		try {
			IndexFilter indexFilter = new IndexFilter("color", null);
			manager.find(Bird.class, indexFilter, false);
			fail("It must throw a FloggyException");
		} catch (Exception ex) {
			assertEquals(FloggyException.class, ex.getClass());
		} finally {
		}
	}

	public void testDoesNotFind() throws Exception {
		Vector birds = buildOrderedPersons(100);

		final String colorExpected = "noColor";
		try {
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.save(bird);
			}

			IndexFilter indexFilter = new IndexFilter("byColor",
					colorExpected);
			ObjectSet os = manager.find(Bird.class, indexFilter, false);

			assertEquals(0, os.size());

		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			IndexManager.shutdown();
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.delete(bird);
			}
		}
	}

	public void testFindOne() throws Exception {
		Vector birds = buildOrderedPersons(100);

		final String colorExpected = "color98";
		try {
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.save(bird);
			}

			IndexFilter indexFilter = new IndexFilter("byColor",
					colorExpected);
			ObjectSet os = manager.find(Bird.class, indexFilter, false);

			assertEquals(1, os.size());
			assertEquals(colorExpected, ((Bird) os.get(0)).getColor());

		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.delete(bird);
			}
		}
	}

	public void testFindThree() throws Exception {
		Vector birds = buildOrderedPersons(100);

		final String colorExpected = "color98";
		try {
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.save(bird);
			}

			Bird bird1 = new Bird();
			bird1.setColor(colorExpected);
			manager.save(bird1);

			Bird bird2 = new Bird();
			bird2.setColor(colorExpected);
			manager.save(bird2);

			IndexFilter indexFilter = new IndexFilter("byColor",
					colorExpected);
			ObjectSet os = manager.find(Bird.class, indexFilter, false);

			assertEquals(3, os.size());
			assertEquals(colorExpected, ((Bird) os.get(0)).getColor());
			assertEquals(colorExpected, ((Bird) os.get(1)).getColor());

			manager.delete(bird1);
			manager.delete(bird2);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.delete(bird);
			}
		}
	}

	public void testFindTwo() throws Exception {
		Vector birds = buildOrderedPersons(100);

		final String colorExpected = "color98";
		try {
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.save(bird);
			}

			Bird bird = new Bird();
			bird.setColor(colorExpected);
			manager.save(bird);

			IndexFilter indexFilter = new IndexFilter("byColor",
					colorExpected);
			ObjectSet os = manager.find(Bird.class, indexFilter, false);

			assertEquals(2, os.size());
			assertEquals(colorExpected, ((Bird) os.get(0)).getColor());
			assertEquals(colorExpected, ((Bird) os.get(1)).getColor());

			manager.delete(bird);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			for (Iterator iterator = birds.iterator(); iterator.hasNext();) {
				Bird bird = (Bird) iterator.next();
				manager.delete(bird);
			}
		}
	}
	
	public void testSaveNotNullThenNullAndFind() throws Exception {
		String name = "New York";

		FR2935390 fr2935390 = new FR2935390();
		fr2935390.setName(name);

		try {
			manager.save(fr2935390);

			IndexFilter filter = new IndexFilter("byName", name);
			ObjectSet os = manager.find(FR2935390.class, filter, false);
			assertEquals(1, os.size());
			
			fr2935390.setName(null);
			manager.save(fr2935390);
			
			os = manager.find(FR2935390.class, filter, false);
			assertEquals(0, os.size());
			
		} finally {
			manager.delete(fr2935390);
		}
	}
	
	public void testFindWithoutAddingPersistables() throws Exception {
		String name = "Parsippany";

//		FR2935390 fr2935390 = new FR2935390();
//		fr2935390.setName(name);
//		manager.save(fr2935390);
//		
//		fr2935390 = new FR2935390();
//		fr2935390.setName(name);
//		manager.save(fr2935390);
//
//		IndexManager.shutdown();

		MicroEmulator oldEmulator = MIDletBridge.getMicroEmulator();
		
		try {
			FileUtils.forceMkdir(new File("target/fr2935390/rms/1.4.0"));
			IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/FR2935390.rms"), new FileOutputStream("target/fr2935390/rms/1.4.0/FR2935390.rms"));
			IOUtils.copy(new FileInputStream("src/test/rms/1.4.0/Index1452747138byName.rms"), new FileOutputStream("target/fr2935390/rms/1.4.0/Index1452747138byName.rms"));
			MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator("target/fr2935390/rms/1.4.0"));
			RecordStoreManager.reset();
			IndexManager.reset();
			PersistableMetadataManager.init();
			IndexManager.init();
		
			IndexFilter filter = new IndexFilter("byName", name);
			ObjectSet os = manager.find(FR2935390.class, filter, false);
			
			assertEquals(2, os.size());
		} finally {
			MIDletBridge.setMicroEmulator(oldEmulator);
		}
	}

	public void testIndexManagerShutdownMethod() throws Exception {
		MicroEmulator oldEmulator = MIDletBridge.getMicroEmulator();
		String name = "New York";

		FR2935390 fr2935390 = new FR2935390();
		fr2935390.setName(name);

		try {
			FileUtils.forceMkdir(new File("target/fr2935390/rms/1.4.0"));
			MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator("target/fr2935390/rms/1.4.0"));
			IndexManager.reset();

			manager.save(fr2935390);
			
			IndexManager.shutdown();
			
		
			IndexFilter filter = new IndexFilter("byName", name);
			ObjectSet os = manager.find(FR2935390.class, filter, false);
			assertEquals(1, os.size());
			
			manager.delete(fr2935390);
			
			os = manager.find(FR2935390.class, filter, false);
			assertEquals(0, os.size());
			
			IndexManager.shutdown();
			IndexManager.reset();
			IndexManager.init();
			
			os = manager.find(FR2935390.class, filter, false);
			assertEquals(0, os.size());
		} finally {
			manager.delete(fr2935390);
			MIDletBridge.setMicroEmulator(oldEmulator);
		}
	}

}
