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
package net.sourceforge.floggy.persistence;

import java.util.List;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class ConfigurationTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*/
	public void testAddPersistableMetadata() {
		String className = "test";
		Configuration configuration = new Configuration();
		PersistableMetadata metadata =
			new PersistableMetadata(false, className, null, null, null, null, null,
				null, 0, null);

		configuration.addPersistableMetadata(metadata);

		PersistableMetadata temp = configuration.getPersistableMetadata(className);

		assertSame(metadata, temp);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testContainsPersistable() {
		String className = "test";
		Configuration configuration = new Configuration();
		PersistableMetadata metadata =
			new PersistableMetadata(false, className, null, null, null, null, null,
				null, 0, null);

		configuration.addPersistableMetadata(metadata);

		assertTrue(configuration.containsPersistable(className));
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetPersistableMetadata() {
		String className = "test";
		Configuration configuration = new Configuration();
		PersistableMetadata metadata =
			new PersistableMetadata(false, className, null, null, null, null, null,
				null, 0, null);

		configuration.addPersistableMetadata(metadata);

		PersistableMetadata temp = configuration.getPersistableMetadata(className);

		assertSame(metadata, temp);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetPersistableMetadatas() {
		Configuration configuration = new Configuration();
		List list = configuration.getPersistableMetadatas();

		assertNotNull(list);
		assertEquals(0, list.size());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testIsAddDefaultConstructor() {
		Configuration configuration = new Configuration();

		assertTrue(configuration.isAddDefaultConstructor());

		configuration.setAddDefaultConstructor(false);

		assertFalse(configuration.isAddDefaultConstructor());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testIsGenerateSource() {
		Configuration configuration = new Configuration();

		assertFalse(configuration.isGenerateSource());

		configuration.setGenerateSource(true);

		assertTrue(configuration.isGenerateSource());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testSetAddDefaultConstructor() {
		Configuration configuration = new Configuration();

		assertTrue(configuration.isAddDefaultConstructor());

		configuration.setAddDefaultConstructor(false);

		assertFalse(configuration.isAddDefaultConstructor());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testSetGenerateSource() {
		Configuration configuration = new Configuration();

		assertFalse(configuration.isGenerateSource());

		configuration.setGenerateSource(true);

		assertTrue(configuration.isGenerateSource());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testSetPersistables() {
		Configuration configuration = new Configuration();
		List list = configuration.getPersistableMetadatas();

		assertNotNull(list);
		assertEquals(0, list.size());

		configuration.setPersistables(null);

		list = configuration.getPersistableMetadatas();

		assertNull(list);
	}
}
