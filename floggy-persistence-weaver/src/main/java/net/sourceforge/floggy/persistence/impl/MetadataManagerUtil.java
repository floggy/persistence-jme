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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class MetadataManagerUtil {

	public static final String VERSION_1_0_1 = "1.0.1";
	public static final String VERSION_1_1_0 = "1.1.0";
	public static final String VERSION_1_1_1 = "1.1.1";
	public static final String VERSION_1_2_0 = "1.2.0";
	public static final String VERSION_1_3_0 = "1.3.0-SNAPSHOT";
	public static final String CURRENT_VERSION = VERSION_1_3_0;

	private static Hashtable metadatas;
	private static String rmsVersion;

	private static void deserialize(byte[] data) throws IOException {
		DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(data));
		while (dis.available() != 0) {
			String field = dis.readUTF();
			if (field.equals("version")) {
				rmsVersion = dis.readUTF();
			}
		}
	}

	public static String getBytecodeVersion() {
		return CURRENT_VERSION;
	}

	public static PersistableMetadata getMetadata(String className) {
		return (PersistableMetadata) metadatas.get(className);
	}

	public static String getRMSVersion() {
		return rmsVersion;
	}

	public static void init() throws IOException, RecordStoreException {
	}

	public static void load() throws IOException, RecordStoreException {
		RecordStore rs = RecordStore.openRecordStore("FloggyProperties", true);
		if (rs.getNumRecords() != 0) {
			deserialize(rs.getRecord(1));
		} else {
			rmsVersion = CURRENT_VERSION;
			save(rs);
		}
	}

	private static void save(RecordStore rs) throws IOException,
			RecordStoreException {
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
