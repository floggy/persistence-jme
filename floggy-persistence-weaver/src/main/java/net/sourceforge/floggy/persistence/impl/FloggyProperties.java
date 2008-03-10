package net.sourceforge.floggy.persistence.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

public class FloggyProperties {

	public static final String VERSION_1_0_1 = "1.0.1";
	public static final String VERSION_1_1_0 = "1.1.0";
	public static final String CURRENT_VERSION = VERSION_1_1_0;
	private static FloggyProperties instance;
	
	public static FloggyProperties getInstance() throws IOException,
			RecordStoreException {
		if (instance == null) {
			instance = new FloggyProperties();
			instance.load();
		}
		return instance;
	}

	protected String version = CURRENT_VERSION;

	private FloggyProperties() {

	}

	protected void deserialize(byte[] data) throws IOException {
		DataInputStream dis = new DataInputStream(
				new ByteArrayInputStream(data));
		while (dis.available() != 0) {
			String field = dis.readUTF();
			if (field.equals("version")) {
				version = dis.readUTF();
			}
		}
	}

	public String getVersion() {
		return version;
	}

	public void load() throws IOException, RecordStoreException {
		RecordStore rs = RecordStore.openRecordStore("FloggyProperties", true);
		if (rs.getNumRecords() != 0) {
			deserialize(rs.getRecord(1));
		} else {
			save(rs);
		}
	}

	public void save() throws IOException, RecordStoreException {
		RecordStore rs = RecordStore.openRecordStore("FloggyProperties", true);
		save(rs);
	}

	protected void save(RecordStore rs) throws IOException,
			RecordStoreException {
		byte[] data = serialize();
		if (rs.getNumRecords() == 1) {
			rs.setRecord(1, data, 0, data.length);
		} else {
			rs.addRecord(data, 0, data.length);
		}
	}

	protected byte[] serialize() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();
		fos.writeUTF("version");
		fos.writeUTF(version);
		return fos.toByteArray();
	}

}
