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
	
	private String className;
	private String superClassName;
	private String[] fieldNames;
	private int[] fieldTypes;
	private String recordStoreName;
	private int recordId;
	
	
	public PersistableMetadata(String className, String superClassName, String[] fieldNames, int[] fieldTypes, String recordStoreName) {
		this(className, superClassName, fieldNames, fieldTypes, recordStoreName, -1);
	}

	public PersistableMetadata(String className, String superClassName, String[] fieldNames, int[] fieldTypes, String recordStoreName, int recordId) {
		super();
		this.className = className;
		this.superClassName = superClassName;
		this.fieldNames = fieldNames;
		this.fieldTypes = fieldTypes;
		this.recordStoreName = recordStoreName;
		this.recordId = recordId;
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
	
	public int getRecordId() {
		return recordId;
	}
	
	public String getRecordStoreName() {
		return recordStoreName;
	}
	
	public String getSuperClassName() {
		return superClassName;
	}

}
