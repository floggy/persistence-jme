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

import java.util.Hashtable;
import java.util.Vector;

/**
* <b>IMPORTANT:</b> This class is for internal use only.
 */
public class PersistableMetadata {
	public static final int BOOLEAN = 1;
	public static final int BYTE = 2;
	public static final int CALENDAR = 4;
	public static final int CHARACTER = 8;
	public static final int DATE = 16;
	public static final int DOUBLE = 32;
	public static final int FLOAT = 64;
	public static final int HASHTABLE = 128;
	public static final int INT = 256;
	public static final int LONG = 512;
	public static final int PERSISTABLE = 1024;
	public static final int SHORT = 2048;
	public static final int STACK = 4096;
	public static final int STRING = 8192;
	public static final int STRINGBUFFER = 16384;
	public static final int TIMEZONE = 32768;
	public static final int VECTOR = 65536;
	public static final int ARRAY = 131072;
	public static final int PRIMITIVE = 262144;
	public static final int JOINED_STRATEGY = 1;
	public static final int PER_CLASS_STRATEGY = 2;
	public static final int SINGLE_STRATEGY = 4;
	private Hashtable persistableImplementations;
	private String className;
	private String recordStoreName;
	private String recordStoreVersion;
	private String suiteName;
	private String superClassName;
	private String vendorName;
	private Vector indexMetadatas;
	private String[] fieldNames;
	private int[] fieldTypes;
	private boolean isAbstract;
	private transient int recordId = -1;
	private String[] descendents;
	private int persistableStrategy;

	/**
	 * Creates a new PersistableMetadata object.
	 *
	 * @param isAbstract DOCUMENT ME!
	 * @param className DOCUMENT ME!
	 * @param superClassName DOCUMENT ME!
	 * @param fieldNames DOCUMENT ME!
	 * @param fieldTypes DOCUMENT ME!
	 * @param persistableImplementations DOCUMENT ME!
	 * @param indexMetadatas DOCUMENT ME!
	 * @param recordStoreName DOCUMENT ME!
	 * @param persistableStrategy DOCUMENT ME!
	 * @param descendents DOCUMENT ME!
	 */
	public PersistableMetadata(boolean isAbstract, String className,
		String superClassName, String[] fieldNames, int[] fieldTypes,
		Hashtable persistableImplementations, Vector indexMetadatas,
		String recordStoreName, int persistableStrategy, String[] descendents) {
		this(isAbstract, className, superClassName, fieldNames, fieldTypes,
			persistableImplementations, indexMetadatas, recordStoreName, null,
			persistableStrategy, -1);
		this.descendents = descendents;
	}

	/**
	 * Creates a new PersistableMetadata object.
	 *
	 * @param isAbstract DOCUMENT ME!
	 * @param className DOCUMENT ME!
	 * @param superClassName DOCUMENT ME!
	 * @param fieldNames DOCUMENT ME!
	 * @param fieldTypes DOCUMENT ME!
	 * @param persistableImplementations DOCUMENT ME!
	 * @param indexMetadatas DOCUMENT ME!
	 * @param recordStoreName DOCUMENT ME!
	 * @param recordStoreVersion DOCUMENT ME!
	 * @param persistableStrategy DOCUMENT ME!
	 * @param recordId DOCUMENT ME!
	 */
	public PersistableMetadata(boolean isAbstract, String className,
		String superClassName, String[] fieldNames, int[] fieldTypes,
		Hashtable persistableImplementations, Vector indexMetadatas,
		String recordStoreName, String recordStoreVersion, int persistableStrategy,
		int recordId) {
		super();
		this.isAbstract = isAbstract;
		this.className = className;
		this.superClassName = superClassName;
		this.fieldNames = fieldNames;
		this.fieldTypes = fieldTypes;
		this.persistableImplementations = persistableImplementations;
		this.indexMetadatas = indexMetadatas;
		this.recordStoreName = recordStoreName;
		this.recordStoreVersion = recordStoreVersion;
		this.persistableStrategy = persistableStrategy;
		this.recordId = recordId;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param obj DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		PersistableMetadata other = (PersistableMetadata) obj;

		if (className == null) {
			if (other.className != null)
				return false;
		} else if (!className.equals(other.className))
			return false;

		if (!Utils.equals(fieldNames, other.fieldNames))
			return false;

		if (!Utils.equals(fieldTypes, other.fieldTypes))
			return false;

		if (isAbstract != other.isAbstract)
			return false;

		if (PersistableMetadataManager.VERSION_1_4_0.equals(
				 PersistableMetadataManager.getRMSVersion())) {
			if (indexMetadatas == null) {
				if (other.indexMetadatas != null)
					return false;
			} else if (!Utils.equals(indexMetadatas, other.indexMetadatas))
				return false;
		}

		if (persistableImplementations == null) {
			if (other.persistableImplementations != null)
				return false;
		} else if (!Utils.equals(persistableImplementations,
				 other.persistableImplementations))
			return false;

		if (persistableStrategy != other.persistableStrategy)
			return false;

		if (recordStoreName == null) {
			if (other.recordStoreName != null)
				return false;
		} else if (!recordStoreName.equals(other.recordStoreName))
			return false;

		if (superClassName == null) {
			if (other.superClassName != null)
				return false;
		} else if (!superClassName.equals(other.superClassName))
			return false;

		return true;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getClassName() {
		return className;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String[] getDescendents() {
		return descendents;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String[] getFieldNames() {
		return fieldNames;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int[] getFieldTypes() {
		return fieldTypes;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Vector getIndexMetadatas() {
		return indexMetadatas;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fieldName DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getPersistableImplementationClassForField(String fieldName) {
		return (String) persistableImplementations.get(fieldName);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Hashtable getPersistableImplementations() {
		return persistableImplementations;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getPersistableStrategy() {
		return persistableStrategy;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getRecordId() {
		return recordId;
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
	public String getRecordStoreVersion() {
		return recordStoreVersion;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getSuiteName() {
		return suiteName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getSuperClassName() {
		return superClassName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result)
			+ ((className == null) ? 0 : className.hashCode());
		result = (prime * result) + Utils.hashCode(fieldNames);
		result = (prime * result) + Utils.hashCode(fieldTypes);
		result = (prime * result) + (isAbstract ? 1231 : 1237);
		result = (prime * result)
			+ ((persistableImplementations == null) ? 0
			: persistableImplementations.hashCode());

		if (PersistableMetadataManager.VERSION_1_4_0.equals(
				 PersistableMetadataManager.getRMSVersion())) {
			result = (prime * result)
				+ ((indexMetadatas == null) ? 0 : indexMetadatas.hashCode());
		}

		result = (prime * result) + persistableStrategy;
		result = (prime * result)
			+ ((recordStoreName == null) ? 0 : recordStoreName.hashCode());
		result = (prime * result)
			+ ((superClassName == null) ? 0 : superClassName.hashCode());

		return result;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean isAbstract() {
		return isAbstract;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param descendents DOCUMENT ME!
	*/
	public void setDescendents(String[] descendents) {
		this.descendents = descendents;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param indexMetadatas DOCUMENT ME!
	*/
	public void setIndexMetadatas(Vector indexMetadatas) {
		this.indexMetadatas = indexMetadatas;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistableStrategy DOCUMENT ME!
	*/
	public void setPersistableStrategy(int persistableStrategy) {
		this.persistableStrategy = persistableStrategy;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param recordId DOCUMENT ME!
	*/
	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param recordStoreName DOCUMENT ME!
	*/
	public void setRecordStoreName(String recordStoreName) {
		this.recordStoreName = recordStoreName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param recordStoreVersion DOCUMENT ME!
	*/
	public void setRecordStoreVersion(String recordStoreVersion) {
		this.recordStoreVersion = recordStoreVersion;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param suiteName DOCUMENT ME!
	*/
	public void setSuiteName(String suiteName) {
		this.suiteName = suiteName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param vendorName DOCUMENT ME!
	*/
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String toString() {
		return "PersistableMetadata [isAbstract=" + isAbstract + ", className="
		+ className + ", superClassName=" + superClassName + ", fieldNames="
		+ ((fieldNames != null) ? arrayToString(fieldNames, fieldNames.length) : null)
		+ ", fieldTypes="
		+ ((fieldTypes != null) ? arrayToString(fieldTypes, fieldTypes.length) : null)
		+ ", persistableImplementations=" + persistableImplementations
		+ ", indexMetadatas=" + indexMetadatas + ", recordStoreName="
		+ recordStoreName + ", persistableStrategy=" + persistableStrategy + "]";
	}

	private String arrayToString(Object array2, int len) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");

		for (int i = 0; i < len; i++) {
			if (i > 0)
				buffer.append(", ");

			if (array2 instanceof int[])
				buffer.append(((int[]) array2)[i]);

			if (array2 instanceof Object[])
				buffer.append(((Object[]) array2)[i]);
		}

		buffer.append("]");

		return buffer.toString();
	}
}
