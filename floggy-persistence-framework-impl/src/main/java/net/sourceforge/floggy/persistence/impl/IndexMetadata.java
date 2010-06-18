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

package net.sourceforge.floggy.persistence.impl;

import java.util.Vector;

/**
 * 
 * An internal <code>class</code> that encapsulates properties of the index.
 * 
 * The recordStoreName field must be a unique between al the application.
 * 
 * @since 1.4
 */
public class IndexMetadata {

	private String recordStoreName;
	private String name;
	private Vector fields;

	public IndexMetadata(String recordStoreName, String name, Vector fields) {
		this.recordStoreName = recordStoreName;
		this.name = name;
		this.fields = fields;
	}

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

	public Vector getFields() {
		return fields;
	}

	public String getId() {
		return recordStoreName;
	}

	public String getName() {
		return name;
	}

	public String getRecordStoreName() {
		return recordStoreName;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fields == null) ? 0 : fields.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((recordStoreName == null) ? 0 : recordStoreName.hashCode());
		return result;
	}

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

	public void setFields(Vector fields) {
		this.fields = fields;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setRecordStoreName(String name) {
		this.recordStoreName = name;
	}

	public String toString() {
		return "IndexMetadata [recordStoreName=" + recordStoreName + ", name="
				+ name + ", field=" + fields + "]";
	}

}
