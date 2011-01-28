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

import java.util.Vector;

/**
* An internal <code>class</code> that encapsulates properties of the index.
* The recordStoreName field must be a unique between al the application.
*
* @since 1.4
 */
public class IndexMetadata {
	private String name;
	private String recordStoreName;
	private Vector fields;

	/**
	 * Creates a new IndexMetadata object.
	 *
	 * @param recordStoreName DOCUMENT ME!
	 * @param name DOCUMENT ME!
	 * @param fields DOCUMENT ME!
	 */
	public IndexMetadata(String recordStoreName, String name, Vector fields) {
		this.recordStoreName = recordStoreName;
		this.name = name;
		this.fields = fields;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param obj DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (getClass() != obj.getClass()) {
			return false;
		}

		IndexMetadata other = (IndexMetadata) obj;

		if (fields == null) {
			if (other.fields != null) {
				return false;
			}
		} else if (!Utils.equals(fields, other.fields)) {
			return false;
		}

		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}

		if (recordStoreName == null) {
			if (other.recordStoreName != null) {
				return false;
			}
		} else if (!recordStoreName.equals(other.recordStoreName)) {
			return false;
		}

		return true;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Vector getFields() {
		return fields;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getId() {
		return recordStoreName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getRecordStoreName() {
		return recordStoreName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((fields == null) ? 0 : fields.hashCode());
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result)
			+ ((recordStoreName == null) ? 0 : recordStoreName.hashCode());

		return result;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param indexMetadata DOCUMENT ME!
	*/
	public void merge(IndexMetadata indexMetadata) {
		String recordStoreName = indexMetadata.getRecordStoreName();

		if (!Utils.isEmpty(recordStoreName)) {
			setRecordStoreName(recordStoreName);
		}

		String name = indexMetadata.getName();

		if (!Utils.isEmpty(name)) {
			setName(name);
		}

		Vector fields = indexMetadata.getFields();

		if (fields != null) {
			setFields(fields);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fields DOCUMENT ME!
	*/
	public void setFields(Vector fields) {
		this.fields = fields;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param name DOCUMENT ME!
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param name DOCUMENT ME!
	*/
	public void setRecordStoreName(String name) {
		this.recordStoreName = name;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String toString() {
		return "IndexMetadata [recordStoreName=" + recordStoreName + ", name="
		+ name + ", field=" + fields + "]";
	}
}
