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

import junit.framework.TestCase;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class IndexFilterTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*/
	public void testGetIndexValue() {
		String indexName = "testIndex";
		Object indexValue = new Integer(4545);
		IndexFilter filter = new IndexFilter(indexName, indexValue);

		assertSame(indexValue, filter.getIndexValue());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testIndexFilterEmptyIndexName() {
		try {
			new IndexFilter("", null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testIndexFilterNullIndexName() {
		try {
			new IndexFilter(null, null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testIndexFilterNullIndexValue() {
		String indexName = "testIndex";

		try {
			new IndexFilter(indexName, null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testSetIndexName() {
		try {
			IndexFilter filter = new IndexFilter("test", "indexValue");
			filter.setIndexName(null);
			fail("It must throw an IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testSetIndexValue() {
		String indexName = "testIndex";
		Object indexValue = new Integer(4545);
		IndexFilter filter = new IndexFilter(indexName, new Integer(23));

		filter.setIndexValue(indexValue);
		assertSame(indexValue, filter.getIndexValue());
	}
}
