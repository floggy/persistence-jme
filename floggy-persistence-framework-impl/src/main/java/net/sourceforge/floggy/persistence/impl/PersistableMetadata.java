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
package net.sourceforge.floggy.persistence.impl;

import java.util.Enumeration;
import java.util.Hashtable;

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

    private static boolean equals(Hashtable h, Hashtable h2) {
		if (h == h2)
			return true;
		if (h == null || h2 == null)
			return false;

		if (h.size() != h2.size())
			return false;

		Enumeration keys = h.keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = h.get(key);
			if (value == null) {
				if (!(h2.get(key) == null && h2.containsKey(key)))
					return false;
			} else {
				if (!value.equals(h2.get(key)))
					return false;
			}
		}

		return true;
	}

	private static boolean equals(int[] a, int[] a2) {
		if (a == a2)
			return true;
		if (a == null || a2 == null)
			return false;

		int length = a.length;
		if (a2.length != length)
			return false;

		for (int i = 0; i < length; i++)
			if (a[i] != a2[i])
				return false;

		return true;
	}

	private static boolean equals(String[] a, String[] a2) {
		if (a == a2)
			return true;
		if (a == null || a2 == null)
			return false;

		int length = a.length;
		if (a2.length != length)
			return false;

		for (int i = 0; i < length; i++) {
			Object o1 = a[i];
			Object o2 = a2[i];
			if (!(o1 == null ? o2 == null : o1.equals(o2)))
				return false;
		}

		return true;
	}

	/**
	 * Returns a hash code value for the array
	 * 
	 * @param array
	 *            the array to create a hash code value for
	 * @return a hash code value for the array
	 */
	private static int hashCode(int[] array) {
		final int prime = 31;
		if (array == null)
			return 0;
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = prime * result + array[index];
		}
		return result;
	}

	/**
	 * Returns a hash code value for the array
	 * 
	 * @param array
	 *            the array to create a hash code value for
	 * @return a hash code value for the array
	 */
	private static int hashCode(Object[] array) {
		final int prime = 31;
		if (array == null)
			return 0;
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = prime * result
					+ (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}

	private boolean isAbstract;
	private String className;
	private String superClassName;
	private String[] fieldNames;
	private int[] fieldTypes;
	private Hashtable persistableImplementations;
	private String recordStoreName;
	private transient int recordId = -1;
	private int persistableStrategy;

    public PersistableMetadata(boolean isAbstract, String className,
			String superClassName, String[] fieldNames, int[] fieldTypes,
			Hashtable persistableImplementations, String recordStoreName, int persistableStrategy) {
		this(isAbstract, className, superClassName, fieldNames, fieldTypes,
				persistableImplementations, recordStoreName, persistableStrategy, -1);
	}

    public PersistableMetadata(boolean isAbstract, String className,
			String superClassName, String[] fieldNames, int[] fieldTypes,
			Hashtable persistableImplementations, String recordStoreName,
			int persistableStrategy, int recordId) {
		super();
		this.isAbstract = isAbstract;
		this.className = className;
		this.superClassName = superClassName;
		this.fieldNames = fieldNames;
		this.fieldTypes = fieldTypes;
		this.persistableImplementations = persistableImplementations;
		this.recordStoreName = recordStoreName;
		this.persistableStrategy = persistableStrategy;
		this.recordId = recordId;
	}

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
		if (!PersistableMetadata.equals(fieldNames, other.fieldNames))
			return false;
		if (!PersistableMetadata.equals(fieldTypes, other.fieldTypes))
			return false;
		if (isAbstract != other.isAbstract)
			return false;
		if (persistableImplementations == null) {
			if (other.persistableImplementations != null)
				return false;
		} else if (!PersistableMetadata.equals(persistableImplementations,
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

	public String getClassName() {
		return className;
	}

	public String[] getFieldNames() {
		return fieldNames;
	}

	public int[] getFieldTypes() {
		return fieldTypes;
	}

	public String getPersistableImplementationClassForField(String fieldName) {
		return (String) persistableImplementations.get(fieldName);
	}

	public Hashtable getPersistableImplementations() {
		return persistableImplementations;
	}
	
	public int getPersistableStrategy() {
		return persistableStrategy;
	}

	public int getRecordId() {
		return recordId;
	}

	public String getRecordStoreName() {
		return recordStoreName;
	}

	public String getSuperClassName() {
		return superClassName;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((className == null) ? 0 : className.hashCode());
		result = prime * result + PersistableMetadata.hashCode(fieldNames);
		result = prime * result + PersistableMetadata.hashCode(fieldTypes);
		result = prime * result + (isAbstract ? 1231 : 1237);
		result = prime
				* result
				+ ((persistableImplementations == null) ? 0
						: persistableImplementations.hashCode());
		result = prime * result + persistableStrategy;
		result = prime * result
				+ ((recordStoreName == null) ? 0 : recordStoreName.hashCode());
		result = prime * result
				+ ((superClassName == null) ? 0 : superClassName.hashCode());
		return result;
	}

	public boolean isAbstract() {
		return isAbstract;
	}
	
	public void setPersistableStrategy(int persistableStrategy) {
		this.persistableStrategy = persistableStrategy;
	}

	public void setRecordId(int recordId) {
		this.recordId = recordId;
	}

	public void setRecordStoreName(String recordStoreName) {
		this.recordStoreName = recordStoreName;
	}

	public String toString() {
		return "PersistableMetadata [isAbstract="
				+ isAbstract
				+ ", className="
				+ className
				+ ", superClassName="
				+ superClassName
				+ ", fieldNames="
				+ (fieldNames != null ? arrayToString(fieldNames,
						fieldNames.length) : null)
				+ ", fieldTypes="
				+ (fieldTypes != null ? arrayToString(fieldTypes,
						fieldTypes.length) : null)
				+ ", persistableImplementations=" + persistableImplementations
				+ ", recordStoreName=" + recordStoreName + ", recordId="
				+ recordId + ", persistableStrategy=" + persistableStrategy
				+ "]";
	}

	private String arrayToString(Object array, int len) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		for (int i = 0; i < len; i++) {
			if (i > 0)
				buffer.append(", ");
			if (array instanceof int[])
				buffer.append(((int[]) array)[i]);
			if (array instanceof Object[])
				buffer.append(((Object[]) array)[i]);
		}
		buffer.append("]");
		return buffer.toString();
	}

}
