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

package net.sourceforge.floggy.persistence.migration;

/**
 * Represents a Persistable information in the update process.
 * 
 * @author Thiago Moreira
 * 
 * @since 1.3.0
 */
public class FieldPersistableInfo {
	/**
	 * The Persistable's class name
	 */
	protected String className;

	/**
	 * The Persistable's id
	 */
	protected int id;

	/**
	 * Creates a new FieldPersistableInfo object.
	 * 
	 * @param id
	 *            The Persistable's id
	 * @param className
	 *            The Persistable's class name
	 */
	public FieldPersistableInfo(int id, String className) {
		super();
		this.id = id;
		this.className = className;
	}

	/**
	 * Gets the class name
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Gets the id
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
				+ ((className == null) ? 0 : className.hashCode());
		result = (prime * result) + id;

		return result;
	}

	/**
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		final FieldPersistableInfo other = (FieldPersistableInfo) obj;

		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;

		if (id != other.id)
			return false;

		return true;
	}

	/**
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		StringBuffer result = new StringBuffer();
		String NEW_LINE = System.getProperty("line.separator");
		result.append(this.getClass().getName() + " {" + NEW_LINE);
		result.append(" className: " + className + NEW_LINE);
		result.append(" id: " + id + NEW_LINE);
		result.append("}");

		return result.toString();
	}
}
