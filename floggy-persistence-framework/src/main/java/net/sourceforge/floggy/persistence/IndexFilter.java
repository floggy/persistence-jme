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

package net.sourceforge.floggy.persistence;

/**
 * An class defining a filter which matches a objects (based on an
 * application-defined index). ....
 * 
 * @since 1.4
 */
public class IndexFilter {

	public String indexName;
	public Object indexValue;

	public IndexFilter(String indexName, Object indexValue) {
		setIndexName(indexName);
		setIndexValue(indexValue);
	}

	public String getIndexName() {
		return indexName;
	}

	public void setIndexName(String indexName) {
		if (indexName == null || indexName.trim().length() == 0) {
			throw new IllegalArgumentException("Index name is null!");
		}
		this.indexName = indexName;
	}

	public Object getIndexValue() {
		return indexValue;
	}

	public void setIndexValue(Object indexValue) {
		if (indexValue == null) {
			throw new IllegalArgumentException("Index value is null!");
		}
		this.indexValue = indexValue;
	}

}
