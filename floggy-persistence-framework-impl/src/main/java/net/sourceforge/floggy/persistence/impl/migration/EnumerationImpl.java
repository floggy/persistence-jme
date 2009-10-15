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
package net.sourceforge.floggy.persistence.impl.migration;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.impl.MetadataManagerUtil;
import net.sourceforge.floggy.persistence.impl.PersistableManagerImpl;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.SerializationHelper;
import net.sourceforge.floggy.persistence.impl.__Persistable;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;

public class EnumerationImpl implements Enumeration {

	protected PersistableMetadata classBasedMetadata;
	protected RecordEnumeration enumeration;
	protected PersistableManager manager = PersistableManager.getInstance();
	protected RecordStore recordStore;
	protected PersistableMetadata rmsBasedMetadata;
	protected boolean lazy;
	protected boolean iterationMode;
	//
	protected Hashtable superClassesIDs = new Hashtable();
	protected int recordId = -1;

	EnumerationImpl(PersistableMetadata rmsBasedMetadata, PersistableMetadata classBasedMetadata,
			RecordEnumeration enumeration, RecordStore recordStore, boolean lazy, boolean iterationMode) {
		this.rmsBasedMetadata = rmsBasedMetadata;
		this.classBasedMetadata = classBasedMetadata;
		this.enumeration = enumeration;
		this.recordStore = recordStore;
		this.lazy = lazy;
		this.iterationMode = iterationMode;
	}

	protected void buildPersistable(PersistableMetadata metadata, byte[] data, Hashtable hashtable) throws Exception {
		String[] fieldNames = metadata.getFieldNames();
		int[] fieldTypes = metadata.getFieldTypes();

		if (data != null) {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					data));
			String superClassName = metadata.getSuperClassName();
			if (superClassName != null) {
				int id = dis.readInt();
				PersistableMetadata superMetadata = MetadataManagerUtil.getRMSBasedMetadata(superClassName);
				if (superMetadata == null) {
					superMetadata = MetadataManagerUtil.getClassBasedMetadata(superClassName);
				}
				RecordStore store = PersistableManagerImpl.getRecordStore(superMetadata.getRecordStoreName(), superMetadata);
				byte[] superData = store.getRecord(id);
				buildPersistable(superMetadata, superData, hashtable);
				superClassesIDs.put(superClassName, new Integer(id));
			}

			for (int i = 0; i < fieldNames.length; i++) {
				int type = fieldTypes[i];
				if (lazy) {
					if (((type & PersistableMetadata.PERSISTABLE) != PersistableMetadata.PERSISTABLE)) {
						Object object;
						if ((type & PersistableMetadata.ARRAY) == PersistableMetadata.ARRAY) {
							type = type & ~PersistableMetadata.ARRAY;
							object = readArray(type, fieldNames[i], dis);
						} else if ((type & PersistableMetadata.PRIMITIVE) == PersistableMetadata.PRIMITIVE) {
							type = type & ~PersistableMetadata.PRIMITIVE;
							object = readPrimitive(type, dis);
						} else {
							object = readObject(type, fieldNames[i], dis);
						}
						hashtable.put(fieldNames[i], object);
					}
				} else {
					Object object;
					if ((type & PersistableMetadata.ARRAY) == PersistableMetadata.ARRAY) {
						type = type & ~PersistableMetadata.ARRAY;
						object = readArray(type, fieldNames[i], dis);
					} else if ((type & PersistableMetadata.PRIMITIVE) == PersistableMetadata.PRIMITIVE) {
						type = type & ~PersistableMetadata.PRIMITIVE;
						object = readPrimitive(type, dis);
					} else {
						object = readObject(type, fieldNames[i], dis);
					}
					hashtable.put(fieldNames[i], object);
				}
			}
		}
	}

	protected Object[] createArray(int type, int size) throws FloggyException {
		switch (type) {
		case PersistableMetadata.BOOLEAN:
			return new Boolean[size];
		case PersistableMetadata.BYTE:
			return new Byte[size];
		case PersistableMetadata.CALENDAR:
			return new Calendar[size];
		case PersistableMetadata.CHARACTER:
			return new Character[size];
		case PersistableMetadata.DATE:
			return new Date[size];
		case PersistableMetadata.DOUBLE:
			return new Double[size];
		case PersistableMetadata.FLOAT:
			return new Float[size];
		case PersistableMetadata.HASHTABLE:
			return new Hashtable[size];
		case PersistableMetadata.INT:
			return new Integer[size];
		case PersistableMetadata.LONG:
			return new Long[size];
		case PersistableMetadata.PERSISTABLE:
			return new FieldPersistableInfo[size];
		case PersistableMetadata.SHORT:
			return new Short[size];
		case PersistableMetadata.STACK:
			return new Stack[size];
		case PersistableMetadata.STRING:
			return new String[size];
		case PersistableMetadata.STRINGBUFFER:
			return new StringBuffer[size];
		case PersistableMetadata.TIMEZONE:
			return new TimeZone[size];
		case PersistableMetadata.VECTOR:
			return new Vector[size];
		default:
			throw new FloggyException("Type Unknow: " + type);
		}
	}

	public int delete() throws FloggyException {
		if (recordId != -1) {
			try {
				deleteSuperClassesRegisters();
				recordStore.deleteRecord(recordId);
				int temp = recordId;
				recordId = -1;
				return temp;
			} catch (RecordStoreException ex) {
				throw PersistableManagerImpl.handleException(ex);
			}
		}
		throw new FloggyException("There isn't a register to delete. You have to iterate over the enumeration before call delete.");
	}

	protected void deleteSuperClassesRegisters() throws FloggyException, RecordStoreException  {
		if (!superClassesIDs.isEmpty()) {
			java.util.Enumeration keys = superClassesIDs.keys();
			while (keys.hasMoreElements()) {
				String className = (String) keys.nextElement();
				PersistableMetadata metadata = MetadataManagerUtil.getRMSBasedMetadata(className);
				if (metadata != null) {
					RecordStore recordStore = PersistableManagerImpl.getRecordStore(metadata.getRecordStoreName(), metadata);
					recordStore.deleteRecord(((Integer)superClassesIDs.get(className)).intValue());
				}
			}
		}
	}

	void finish() throws FloggyException {
		if (!enumeration.hasNextElement()) {
			enumeration.destroy();
			PersistableManagerImpl.closeRecordStore(recordStore);
			if (rmsBasedMetadata != classBasedMetadata) {
				try {
					classBasedMetadata.setRecordId(rmsBasedMetadata.getRecordId());
					MetadataManagerUtil.saveRMSStructure(classBasedMetadata);
				} catch (Exception ex) {
					throw PersistableManagerImpl.handleException(ex);
				}
			}
		} else {
			throw new FloggyException("The enumeration must be whole processed before finish the process!");
		}
	}
	
	public int getSize() {
		return enumeration.numRecords();
	}

	public boolean hasMoreElements() {
		return enumeration.hasNextElement();
	}

	public Hashtable nextElement() throws FloggyException {
		if (recordId != -1 && !iterationMode) {
			throw new FloggyException("You should delete or update the current register before step into the next one."); 
		}
		superClassesIDs.clear();
		Hashtable hashtable = new HashtableValueNullable();
		try {
			recordId = enumeration.nextRecordId();
			byte[] data = recordStore.getRecord(recordId);
			buildPersistable(rmsBasedMetadata, data, hashtable);
		} catch (Exception ex) {
			throw PersistableManagerImpl.handleException(ex);
		}
		return hashtable;
	}

	protected Object readArray(int type, String fieldName, DataInputStream dis) throws Exception {
		Object object = null;
		if (dis.readByte() == SerializationHelper.NOT_NULL) {
			int size = dis.readInt();
			if ((type & PersistableMetadata.PRIMITIVE) == PersistableMetadata.PRIMITIVE) {
				type = type & ~PersistableMetadata.PRIMITIVE;
				switch (type) {
				case PersistableMetadata.BOOLEAN:
					object = readBooleanArray(size, dis);
					break;
				case PersistableMetadata.BYTE:
					object = readByteArray(size, dis);
					break;
				case PersistableMetadata.CHARACTER:
					object = readCharArray(size, dis);
					break;
				case PersistableMetadata.DOUBLE:
					object = readDoubleArray(size, dis);
					break;
				case PersistableMetadata.FLOAT:
					object = readFloatArray(size, dis);
					break;
				case PersistableMetadata.INT:
					object = readIntArray(size, dis);
					break;
				case PersistableMetadata.LONG:
					object = readLongArray(size, dis);
					break;
				case PersistableMetadata.SHORT:
					object = readShortArray(size, dis);
					break;
				}
			} else {
				Object[] array = createArray(type, size);
				for (int i = 0; i < size; i++) {
					array[i] = readObject(type, fieldName, dis);
				}
				object = array;
			}
		}
		return object;
	}

	protected boolean[] readBooleanArray(int size, DataInputStream dis)
			throws Exception {
		boolean[] temp = new boolean[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readBoolean();
		}
		return temp;
	}

	protected byte[] readByteArray(int size, DataInputStream dis)
			throws Exception {
		byte[] temp = new byte[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readByte();
		}
		return temp;
	}

	protected char[] readCharArray(int size, DataInputStream dis)
			throws Exception {
		char[] temp = new char[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readChar();
		}
		return temp;
	}

	protected double[] readDoubleArray(int size, DataInputStream dis)
			throws Exception {
		double[] temp = new double[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readDouble();
		}
		return temp;
	}

	protected float[] readFloatArray(int size, DataInputStream dis)
			throws Exception {
		float[] temp = new float[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readFloat();
		}
		return temp;
	}

	protected int[] readIntArray(int size, DataInputStream dis)
			throws Exception {
		int[] temp = new int[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readInt();
		}
		return temp;
	}

	protected long[] readLongArray(int size, DataInputStream dis)
			throws Exception {
		long[] temp = new long[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readLong();
		}
		return temp;
	}

	protected Object readObject(int type, String fieldName, DataInputStream dis) throws Exception {
		switch (type) {
		case PersistableMetadata.BOOLEAN:
			return SerializationHelper.readBoolean(dis);
		case PersistableMetadata.BYTE:
			return SerializationHelper.readByte(dis);
		case PersistableMetadata.CALENDAR:
			return SerializationHelper.readCalendar(dis);
		case PersistableMetadata.CHARACTER:
			return SerializationHelper.readChar(dis);
		case PersistableMetadata.DATE:
			return SerializationHelper.readDate(dis);
		case PersistableMetadata.DOUBLE:
			return SerializationHelper.readDouble(dis);
		case PersistableMetadata.FLOAT:
			return SerializationHelper.readFloat(dis);
		case PersistableMetadata.HASHTABLE:
			return SerializationHelper.readHashtable(dis);
		case PersistableMetadata.INT:
			return SerializationHelper.readInt(dis);
		case PersistableMetadata.LONG:
			return SerializationHelper.readLong(dis);
		case PersistableMetadata.PERSISTABLE: {
			FieldPersistableInfo fpi = null;
			String fieldClassName = rmsBasedMetadata.getPersistableImplementationClassForField(fieldName);
			switch (dis.readByte()) {
			case -1:
				fieldClassName = dis.readUTF();
			case SerializationHelper.NOT_NULL:
				int fieldId = dis.readInt();
				fpi = new FieldPersistableInfo(fieldId, fieldClassName);
				break;
			}
			return fpi;
		}
		case PersistableMetadata.SHORT:
			return SerializationHelper.readShort(dis);
		case PersistableMetadata.STACK:
			return SerializationHelper.readStack(dis);
		case PersistableMetadata.STRING:
			return SerializationHelper.readString(dis);
		case PersistableMetadata.STRINGBUFFER:
			return SerializationHelper.readStringBuffer(dis);
		case PersistableMetadata.TIMEZONE:
			return SerializationHelper.readTimeZone(dis);
		case PersistableMetadata.VECTOR:
			return SerializationHelper.readVector(dis);
		default:
			throw new FloggyException("Type Unknow: " + type);
		}
	}

	protected Object readPrimitive(int type, DataInputStream dis)
			throws Exception {
		switch (type) {
		case PersistableMetadata.BOOLEAN:
			return dis.readBoolean() ? Boolean.TRUE : Boolean.FALSE;
		case PersistableMetadata.BYTE:
			return new Byte(dis.readByte());
		case PersistableMetadata.CHARACTER:
			return new Character(dis.readChar());
		case PersistableMetadata.DOUBLE:
			return new Double(dis.readDouble());
		case PersistableMetadata.FLOAT:
			return new Float(dis.readFloat());
		case PersistableMetadata.INT:
			return new Integer(dis.readInt());
		case PersistableMetadata.LONG:
			return new Long(dis.readLong());
		case PersistableMetadata.SHORT:
			return new Short(dis.readShort());
		default:
			throw new FloggyException("Type Unknow: " + type);
		}
	}
	
	protected short[] readShortArray(int size, DataInputStream dis)
			throws Exception {
		short[] temp = new short[size];
		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readShort();
		}
		return temp;
	}

	public int update(Persistable persistable) throws FloggyException {
		if (recordId != -1) {
			__Persistable __persistable = PersistableManagerImpl.checkArgumentAndCast(persistable);
			try {
				__persistable.__setId(recordId);
				deleteSuperClassesRegisters();
				int temp = manager.save(__persistable);
				recordId = -1;
				return temp;
			} catch (RecordStoreException ex) {
				throw PersistableManagerImpl.handleException(ex);
			}
		}
		throw new FloggyException("There isn't a register to update. You have to iterate over the enumeration before call update.");
	}

}
