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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class MetadataManagerUtil {
	public static final String VERSION_1_0_1 = "1.0.1";
	public static final String VERSION_1_1_0 = "1.1.0";
	public static final String VERSION_1_1_1 = "1.1.1";
	public static final String VERSION_1_2_0 = "1.2.0";
	public static final String VERSION_1_3_0 = "1.3.0";
	public static final String CURRENT_VERSION = VERSION_1_3_0;
	private static String rmsVersion;
	private static Hashtable classBasedMetadatas;
	private static Hashtable rmsBasedMetadatas;
	private static Vector notMigratedClassNames;

	/**
	 * Creates a new MetadataManagerUtil object.
	 */
	protected MetadataManagerUtil() {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param metadata DOCUMENT ME!
	*/
	public static void addRMSMetadata(PersistableMetadata metadata) {
		rmsBasedMetadatas.put(metadata.getClassName(), metadata);
		notMigratedClassNames.removeElement(metadata.getClassName());
	}

	/**
	 * DOCUMENT ME!
	*
	* @param metadata DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static boolean containsRMSMetadata(PersistableMetadata metadata) {
		return rmsBasedMetadatas.containsKey(metadata.getClassName());
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static String getBytecodeVersion() {
		return CURRENT_VERSION;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static PersistableMetadata getClassBasedMetadata(String className) {
		return (PersistableMetadata) classBasedMetadatas.get(className);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static Vector getNotMigratedClasses() {
		return notMigratedClassNames;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static PersistableMetadata getRMSBasedMetadata(String className) {
		return (PersistableMetadata) rmsBasedMetadatas.get(className);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static String getRMSVersion() {
		return rmsVersion;
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public static void init() throws Exception {
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public static void load() throws Exception {
		notMigratedClassNames = new Vector();

		RecordStore rs = RecordStore.openRecordStore("FloggyProperties", true);

		if (rs.getNumRecords() != 0) {
			deserialize(rs.getRecord(1));
			loadRMSStructure();
		} else {
			rmsVersion = CURRENT_VERSION;
			save(rs);
		}

		if (classBasedMetadatas != null) {
			Enumeration classNames = classBasedMetadatas.keys();

			while (classNames.hasMoreElements()) {
				String className = (String) classNames.nextElement();

				PersistableMetadata classBasedMetadata =
					(PersistableMetadata) classBasedMetadatas.get(className);
				PersistableMetadata rmsBasedMetadata =
					(PersistableMetadata) rmsBasedMetadatas.get(className);

				if (!classBasedMetadata.equals(rmsBasedMetadata)) {
					notMigratedClassNames.addElement(className);
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param metadata DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public static void saveRMSStructure(PersistableMetadata metadata)
		throws Exception {
		String[] fieldNames = metadata.getFieldNames();
		int[] fieldTypes = metadata.getFieldTypes();
		FloggyOutputStream out = new FloggyOutputStream();

		out.writeUTF(metadata.getClassName());
		out.writeBoolean(metadata.isAbstract());
		SerializationHelper.writeString(out, metadata.getSuperClassName());
		out.writeInt(fieldTypes.length);

		for (int i = 0; i < fieldTypes.length; i++) {
			out.writeUTF(fieldNames[i]);
			out.writeInt(fieldTypes[i]);
		}

		SerializationHelper.writeHashtable(out,
			metadata.getPersistableImplementations());
		out.writeUTF(metadata.getRecordStoreName());

		byte[] data = out.toByteArray();
		RecordStore rs = RecordStore.openRecordStore("FloggyProperties", true);

		int recordId = metadata.getRecordId();

		if (recordId != -1) {
			rs.setRecord(recordId, data, 0, data.length);
		} else {
			metadata.setRecordId(rs.addRecord(data, 0, data.length));
		}

		addRMSMetadata(metadata);
	}

	private static void deserialize(byte[] data) throws IOException {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

		while (dis.available() != 0) {
			String field = dis.readUTF();

			if ("version".equals(field)) {
				rmsVersion = dis.readUTF();
			}
		}
	}

	private static void loadRMSStructure() throws Exception {
		RecordStore rs = RecordStore.openRecordStore("FloggyProperties", true);
		RecordEnumeration enumeration = rs.enumerateRecords(null, null, false);

		while (enumeration.hasNextElement()) {
			int recordId = enumeration.nextRecordId();
			DataInputStream dis =
				new DataInputStream(new ByteArrayInputStream(rs.getRecord(recordId)));
			String className = dis.readUTF();

			if (!"version".equals(className)) {
				boolean isAbstract = dis.readBoolean();
				String superClassName = SerializationHelper.readString(dis);
				String[] fieldNames = new String[dis.readInt()];
				int[] fieldTypes = new int[fieldNames.length];

				for (int i = 0; i < fieldNames.length; i++) {
					fieldNames[i] = dis.readUTF();
					fieldTypes[i] = dis.readInt();
				}

				Hashtable persistableImplementations =
					SerializationHelper.readHashtable(dis);
				String recordStoreName = dis.readUTF();
				addRMSMetadata(new PersistableMetadata(isAbstract, className,
						superClassName, fieldNames, fieldTypes, persistableImplementations,
						recordStoreName, recordId));
			}
		}
	}

	private static void save(RecordStore rs)
		throws IOException, RecordStoreException {
		byte[] data = serialize();

		if (rs.getNumRecords() == 1) {
			rs.setRecord(1, data, 0, data.length);
		} else {
			rs.addRecord(data, 0, data.length);
		}
	}

	private static byte[] serialize() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();
		fos.writeUTF("version");
		fos.writeUTF(rmsVersion);

		return fos.toByteArray();
	}
}
