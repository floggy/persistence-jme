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
package net.sourceforge.floggy.persistence.impl;

import junit.framework.TestCase;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class IndexTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*/
	public void testContainsId() {
		int id = 45;
		Object value = "DSI-office";
		Index index = new Index();

		index.put(id, value);

		assertTrue(index.containsId(id));
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testContainsValue() {
		int id = 45;
		Object value = "DSI-office";
		Index index = new Index();

		index.put(id, value);

		assertTrue(index.containsValue(value));
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetIds() {
		Long expected = new Long(9898);
		Index index = new Index();

		IndexEntry indexEntry = new IndexEntry(expected);
		indexEntry.getPersistableIds().addElement(new Integer(1));
		indexEntry.getPersistableIds().addElement(new Integer(2));
		indexEntry.getPersistableIds().addElement(new Integer(6));
		index.put(indexEntry);

		assertEquals(3, index.getIds(expected).length);

		indexEntry = new IndexEntry(new Long(34));
		indexEntry.getPersistableIds().addElement(new Integer(4));
		indexEntry.getPersistableIds().addElement(new Integer(5));
		index.put(indexEntry);

		assertEquals(2, index.getIds(new Long(34)).length);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetIdsString() {
		String expected = "floggy";
		String expected2 = "flo";
		Index index = new Index();

		IndexEntry indexEntry = new IndexEntry(expected);
		indexEntry.getPersistableIds().addElement(new Integer(1));
		indexEntry.getPersistableIds().addElement(new Integer(2));
		indexEntry.getPersistableIds().addElement(new Integer(6));
		index.put(indexEntry);

		assertEquals(3, index.getIds(expected).length);

		indexEntry = new IndexEntry(expected2);
		indexEntry.getPersistableIds().addElement(new Integer(4));
		indexEntry.getPersistableIds().addElement(new Integer(5));
		index.put(indexEntry);

		assertEquals(5, index.getIds(expected2).length);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetIndexEntryInt() {
		String expected = "floggy";
		Index index = new Index();

		index.put(1, expected);
		index.put(2, expected);
		index.put(6, expected);

		IndexEntry indexEntry = index.getIndexEntry(2);

		assertNull(index.getIndexEntry(90));
		assertEquals(expected, indexEntry.getValue());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testGetIndexEntryObject() {
		String expected = "floggy";
		String expected2 = "Thiago";
		Index index = new Index();

		IndexEntry indexEntry = new IndexEntry(expected);
		indexEntry.getPersistableIds().addElement(new Integer(1));
		indexEntry.getPersistableIds().addElement(new Integer(2));
		indexEntry.getPersistableIds().addElement(new Integer(6));
		index.put(indexEntry);

		assertNull(index.getIndexEntry(expected2));

		indexEntry = index.getIndexEntry(expected);

		assertNotNull(indexEntry);

		assertEquals(3, indexEntry.getPersistableIds().size());
		assertEquals(expected, indexEntry.getValue());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPutIndexEntry() {
		String expected = "floggy";
		String expected2 = "Thiago";
		Index index = new Index();

		IndexEntry indexEntry = new IndexEntry(expected);
		indexEntry.getPersistableIds().addElement(new Integer(1));
		indexEntry.getPersistableIds().addElement(new Integer(2));
		indexEntry.getPersistableIds().addElement(new Integer(6));
		index.put(indexEntry);

		assertTrue(index.containsValue(expected));
		assertEquals(3, index.getIds(expected).length);

		indexEntry = new IndexEntry(expected2);
		indexEntry.getPersistableIds().addElement(new Integer(4));
		indexEntry.getPersistableIds().addElement(new Integer(5));
		index.put(indexEntry);

		assertTrue(index.containsValue(expected2));
		assertEquals(2, index.getIds(expected2).length);

		boolean valid = true;
		int[] values = index.getIds(expected);

		for (int i = 0; i < values.length; i++) {
			valid &= ((values[i] == 1) || (values[i] == 2) || (values[i] == 6));
		}

		assertTrue(valid);

		valid = true;
		values = index.getIds(expected2);

		for (int i = 0; i < values.length; i++) {
			valid &= ((values[i] == 4) || (values[i] == 5));
		}

		assertTrue(valid);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPutIntObject() {
		String expected = "floggy";
		String expected2 = "Thiago";
		Index index = new Index();

		index.put(1, expected);
		index.put(2, expected);
		index.put(6, expected);

		assertTrue(index.containsValue(expected));
		assertEquals(3, index.getIds(expected).length);

		index.put(4, expected2);
		index.put(5, expected2);

		assertTrue(index.containsValue(expected2));
		assertEquals(2, index.getIds(expected2).length);

		boolean valid = true;
		int[] values = index.getIds(expected);

		for (int i = 0; i < values.length; i++) {
			valid &= ((values[i] == 1) || (values[i] == 2) || (values[i] == 6));
		}

		assertTrue(valid);

		valid = true;
		values = index.getIds(expected2);

		for (int i = 0; i < values.length; i++) {
			valid &= ((values[i] == 4) || (values[i] == 5));
		}

		assertTrue(valid);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testRemove() {
		String expected = "floggy";
		String expected2 = "Thiago";
		Index index = new Index();

		index.put(1, expected);
		index.put(2, expected);
		index.put(6, expected);

		index.remove(1);

		index.put(4, expected2);
		index.put(5, expected2);

		index.remove(4);

		assertTrue(index.containsValue(expected));
		assertEquals(2, index.getIds(expected).length);

		boolean valid = true;
		int[] values = index.getIds(expected);

		for (int i = 0; i < values.length; i++) {
			valid &= ((values[i] == 2) || (values[i] == 6));
		}

		assertTrue(valid);

		index.remove(2);
		index.remove(6);

		assertEquals(0, index.getIndexEntry(expected).getPersistableIds().size());

		valid = true;
		values = index.getIds(expected2);

		for (int i = 0; i < values.length; i++) {
			valid &= (values[i] == 5);
		}

		assertTrue(valid);
	}
}
