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
package net.sourceforge.floggy.persistence.impl.migration;

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
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;
import net.sourceforge.floggy.persistence.impl.SerializationManager;
import net.sourceforge.floggy.persistence.impl.Utils;
import net.sourceforge.floggy.persistence.impl.__Persistable;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class AbstractEnumerationImpl implements Enumeration {
	/**
	 * DOCUMENT ME!
	 */
	protected PersistableManager manager = PersistableManager.getInstance();

	/**
	 * DOCUMENT ME!
	 */
	protected PersistableMetadata classBasedMetadata;

	/**
	 * DOCUMENT ME!
	 */
	protected PersistableMetadata rmsBasedMetadata;

	/**
	 * DOCUMENT ME!
	 */
	protected RecordEnumeration enumeration;

	/**
	 * DOCUMENT ME!
	 */
	protected RecordStore recordStore;

	/**
	 * DOCUMENT ME!
	 */
	protected boolean iterationMode;

	/**
	 * DOCUMENT ME!
	 */
	protected boolean lazy;

	/**
	 * DOCUMENT ME!
	 */
	protected int recordId = -1;

	/**
	 * Creates a new AbstractEnumerationImpl object.
	 *
	 * @param rmsBasedMetadata DOCUMENT ME!
	 * @param classBasedMetadata DOCUMENT ME!
	 * @param enumeration DOCUMENT ME!
	 * @param recordStore DOCUMENT ME!
	 * @param lazy DOCUMENT ME!
	 * @param iterationMode DOCUMENT ME!
	 *
	 * @throws RecordStoreException DOCUMENT ME!
	 */
	protected AbstractEnumerationImpl(PersistableMetadata rmsBasedMetadata,
		PersistableMetadata classBasedMetadata, RecordEnumeration enumeration,
		RecordStore recordStore, boolean lazy, boolean iterationMode)
		throws RecordStoreException {
		this.rmsBasedMetadata = rmsBasedMetadata;
		this.classBasedMetadata = classBasedMetadata;
		this.enumeration = enumeration;
		this.recordStore = recordStore;
		this.lazy = lazy;
		this.iterationMode = iterationMode;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public int delete() throws FloggyException {
		if (recordId != -1) {
			try {
				recordStore.deleteRecord(recordId);

				int temp = recordId;
				recordId = -1;

				return temp;
			} catch (RecordStoreException ex) {
				throw Utils.handleException(ex);
			}
		}

		throw new FloggyException(
			"There isn't a register to delete. You have to iterate over the enumeration before call delete.");
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getSize() {
		return enumeration.numRecords();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean hasMoreElements() {
		return enumeration.hasNextElement();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public Hashtable nextElement() throws FloggyException {
		if ((recordId != -1) && !iterationMode) {
			throw new FloggyException(
				"You should delete or update the current register before step into the next one.");
		}

		Hashtable hashtable = new HashtableValueNullable();

		try {
			recordId = enumeration.nextRecordId();

			byte[] data = recordStore.getRecord(recordId);
			buildPersistable(rmsBasedMetadata, data, hashtable);
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}

		return hashtable;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistable DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public int update(Persistable persistable) throws FloggyException {
		if (recordId != -1) {
			__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
			__persistable.__setId(recordId);

			int temp = manager.save(__persistable);
			recordId = -1;

			return temp;
		}

		throw new FloggyException(
			"There isn't a register to update. You have to iterate over the enumeration before call update.");
	}

	/**
	 * DOCUMENT ME!
	*
	* @param rmsBasedMetadata DOCUMENT ME!
	* @param data DOCUMENT ME!
	* @param hashtable DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected abstract void buildPersistable(
		PersistableMetadata rmsBasedMetadata, byte[] data, Hashtable hashtable)
		throws Exception;

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param size DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
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

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param size DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	protected Object[] createArrayCLDC10(int type, int size)
		throws FloggyException {
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

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param fieldName DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected Object readArray(int type, String fieldName, DataInputStream dis)
		throws Exception {
		Object object = null;

		if (dis.readByte() == SerializationManager.NOT_NULL) {
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

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param fieldName DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected Object readArrayCLDC10(int type, String fieldName,
		DataInputStream dis) throws Exception {
		Object object = null;

		if (dis.readByte() == SerializationManager.NOT_NULL) {
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

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected boolean[] readBooleanArray(int size, DataInputStream dis)
		throws Exception {
		boolean[] temp = new boolean[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readBoolean();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected byte[] readByteArray(int size, DataInputStream dis)
		throws Exception {
		byte[] temp = new byte[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readByte();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected char[] readCharArray(int size, DataInputStream dis)
		throws Exception {
		char[] temp = new char[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readChar();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected double[] readDoubleArray(int size, DataInputStream dis)
		throws Exception {
		double[] temp = new double[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readDouble();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected float[] readFloatArray(int size, DataInputStream dis)
		throws Exception {
		float[] temp = new float[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readFloat();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected int[] readIntArray(int size, DataInputStream dis)
		throws Exception {
		int[] temp = new int[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readInt();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected long[] readLongArray(int size, DataInputStream dis)
		throws Exception {
		long[] temp = new long[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readLong();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param fieldName DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	* @throws FloggyException DOCUMENT ME!
	*/
	protected Object readObject(int type, String fieldName, DataInputStream dis)
		throws Exception {
		switch (type) {
		case PersistableMetadata.BOOLEAN:
			return SerializationManager.readBoolean(dis);

		case PersistableMetadata.BYTE:
			return SerializationManager.readByte(dis);

		case PersistableMetadata.CALENDAR:
			return SerializationManager.readCalendar(dis);

		case PersistableMetadata.CHARACTER:
			return SerializationManager.readChar(dis);

		case PersistableMetadata.DATE:
			return SerializationManager.readDate(dis);

		case PersistableMetadata.DOUBLE:
			return SerializationManager.readDouble(dis);

		case PersistableMetadata.FLOAT:
			return SerializationManager.readFloat(dis);

		case PersistableMetadata.HASHTABLE:
			return SerializationManager.readHashtable(dis);

		case PersistableMetadata.INT:
			return SerializationManager.readInt(dis);

		case PersistableMetadata.LONG:
			return SerializationManager.readLong(dis);

		case PersistableMetadata.PERSISTABLE: {
			FieldPersistableInfo fpi = null;
			String fieldClassName =
				rmsBasedMetadata.getPersistableImplementationClassForField(fieldName);

			switch (dis.readByte()) {
			case -1:
				fieldClassName = dis.readUTF();

			case SerializationManager.NOT_NULL:

				int fieldId = dis.readInt();
				fpi = new FieldPersistableInfo(fieldId, fieldClassName);

				break;
			}

			return fpi;
		}

		case PersistableMetadata.SHORT:
			return SerializationManager.readShort(dis);

		case PersistableMetadata.STACK:
			return SerializationManager.readStack(dis, lazy);

		case PersistableMetadata.STRING:
			return SerializationManager.readString(dis);

		case PersistableMetadata.STRINGBUFFER:
			return SerializationManager.readStringBuffer(dis);

		case PersistableMetadata.TIMEZONE:
			return SerializationManager.readTimeZone(dis);

		case PersistableMetadata.VECTOR:
			return SerializationManager.readVector(dis, lazy);

		default:
			throw new FloggyException("Type Unknow: " + type);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param fieldName DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	* @throws FloggyException DOCUMENT ME!
	*/
	protected Object readObjectCLDC10(int type, String fieldName,
		DataInputStream dis) throws Exception {
		switch (type) {
		case PersistableMetadata.BOOLEAN:
			return SerializationManager.readBoolean(dis);

		case PersistableMetadata.BYTE:
			return SerializationManager.readByte(dis);

		case PersistableMetadata.CALENDAR:
			return SerializationManager.readCalendar(dis);

		case PersistableMetadata.CHARACTER:
			return SerializationManager.readChar(dis);

		case PersistableMetadata.DATE:
			return SerializationManager.readDate(dis);

		case PersistableMetadata.HASHTABLE:
			return SerializationManager.readHashtable(dis);

		case PersistableMetadata.INT:
			return SerializationManager.readInt(dis);

		case PersistableMetadata.LONG:
			return SerializationManager.readLong(dis);

		case PersistableMetadata.PERSISTABLE: {
			FieldPersistableInfo fpi = null;
			String fieldClassName =
				rmsBasedMetadata.getPersistableImplementationClassForField(fieldName);

			switch (dis.readByte()) {
			case -1:
				fieldClassName = dis.readUTF();

			case SerializationManager.NOT_NULL:

				int fieldId = dis.readInt();
				fpi = new FieldPersistableInfo(fieldId, fieldClassName);

				break;
			}

			return fpi;
		}

		case PersistableMetadata.SHORT:
			return SerializationManager.readShort(dis);

		case PersistableMetadata.STACK:
			return SerializationManager.readStack(dis, lazy);

		case PersistableMetadata.STRING:
			return SerializationManager.readString(dis);

		case PersistableMetadata.STRINGBUFFER:
			return SerializationManager.readStringBuffer(dis);

		case PersistableMetadata.TIMEZONE:
			return SerializationManager.readTimeZone(dis);

		case PersistableMetadata.VECTOR:
			return SerializationManager.readVector(dis, lazy);

		default:
			throw new FloggyException("Type Unknow: " + type);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	* @throws FloggyException DOCUMENT ME!
	*/
	protected Object readPrimitive(int type, DataInputStream dis)
		throws Exception {
		switch (type) {
		case PersistableMetadata.BOOLEAN:
			return dis.readBoolean() ? Utils.TRUE : Utils.FALSE;

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

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	* @throws FloggyException DOCUMENT ME!
	*/
	protected Object readPrimitiveCLDC10(int type, DataInputStream dis)
		throws Exception {
		switch (type) {
		case PersistableMetadata.BOOLEAN:
			return dis.readBoolean() ? Utils.TRUE : Utils.FALSE;

		case PersistableMetadata.BYTE:
			return new Byte(dis.readByte());

		case PersistableMetadata.CHARACTER:
			return new Character(dis.readChar());

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

	/**
	 * DOCUMENT ME!
	*
	* @param size DOCUMENT ME!
	* @param dis DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected short[] readShortArray(int size, DataInputStream dis)
		throws Exception {
		short[] temp = new short[size];

		for (int i = 0; i < temp.length; i++) {
			temp[i] = dis.readShort();
		}

		return temp;
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	void finish() throws FloggyException {
		if (!enumeration.hasNextElement()) {
			enumeration.destroy();
			RecordStoreManager.closeRecordStore(recordStore);

			if (rmsBasedMetadata != classBasedMetadata) {
				try {
					classBasedMetadata.setRecordId(rmsBasedMetadata.getRecordId());
					classBasedMetadata.setRecordStoreVersion(PersistableMetadataManager
						 .getBytecodeVersion());
					PersistableMetadataManager.saveRMSStructure(classBasedMetadata);
				} catch (Exception ex) {
					throw Utils.handleException(ex);
				}
			}
		} else {
			throw new FloggyException(
				"The enumeration must be whole processed before finish the process!");
		}
	}
}
