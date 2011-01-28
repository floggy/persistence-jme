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

/**
* An class defining a filter which matches a objects (based on an
* application-defined index). ....
*
* @since 1.4
 */
public class IndexFilter {
	public Object indexValue;
	public String indexName;

	/**
	 * Creates a new IndexFilter object.
	 *
	 * @param indexName DOCUMENT ME!
	 * @param indexValue DOCUMENT ME!
	 */
	public IndexFilter(String indexName, Object indexValue) {
		setIndexName(indexName);
		setIndexValue(indexValue);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getIndexName() {
		return indexName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getIndexValue() {
		return indexValue;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param indexName DOCUMENT ME!
	*/
	public void setIndexName(String indexName) {
		if ((indexName == null) || (indexName.trim().length() == 0)) {
			throw new IllegalArgumentException("Index name is null!");
		}

		this.indexName = indexName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param indexValue DOCUMENT ME!
	*/
	public void setIndexValue(Object indexValue) {
		if (indexValue == null) {
			throw new IllegalArgumentException("Index value is null!");
		}

		this.indexValue = indexValue;
	}
}
