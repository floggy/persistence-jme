// ME4SE - A MicroEdition Emulation for J2SE 
//
// Copyright (C) 2001 Stefan Haustein, Oberhausen (Rhld.), Germany
//
// Contributors: Michael Kroll
//
// STATUS: 
//
// This program is free software; you can redistribute it and/or
// modify it under the terms of the GNU General Public License as
// published by the Free Software Foundation; either version 2 of the
// License, or (at your option) any later version. This program is
// distributed in the hope that it will be useful, but WITHOUT ANY
// WARRANTY; without even the implied warranty of MERCHANTABILITY or
// FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public
// License for more details. You should have received a copy of the
// GNU General Public License along with this program; if not, write
// to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
// Boston, MA 02111-1307, USA.

package net.sourceforge.floggy.rms4test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

public class RecordStoreImpl_file extends RecordStoreImpl implements FilenameFilter {
	
	private int version = 0;
	private long lastModified = 0L;

	private static final byte[] RECORD_INVALID = new byte[0];

	protected Vector records;
	protected File file;

	// If Applet, set this variable to "null" explicitly.
	// The directory to the RecordStore home directory can 
	// be specified by e.g. -Drms.home="."
	protected File rmsDir; // null for applet

	public RecordStoreImpl_file() {
		String temp= System.getProperty("rms.home");
		if (temp != null) {
			rmsDir= new File(temp);
		}
	}

	boolean isApplet() {
		return rmsDir == null;
	}

	public boolean accept(File dir, String name) {
		return name.endsWith(".rms");
	}

	private void changeVersion() {
		version++;
		lastModified = System.currentTimeMillis();
	}

	private void writeToFile() throws RecordStoreException {
		//System.out.println ("Writing records to file...");

		if (isApplet())
			return;

		try {
			DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));

			dos.writeInt(version);
			dos.writeLong(lastModified);
			int cnt = records.size();
			dos.writeInt(cnt);

			for (int i = 0; i < cnt; i++) {
				Object obj = records.elementAt(i);

				if (obj == RECORD_INVALID) {
					dos.writeInt(-2);
				} else if (obj == null) {
					dos.writeInt(-1);
				} else {
					byte[] buffer = (byte[]) obj;
					dos.writeInt(buffer.length);
					dos.write(buffer, 0, buffer.length);
				}
			}

			dos.flush();
			dos.close();
		} catch (IOException ioe) {
			throw new RecordStoreException("Error writing Records to file!");
		}
		//System.out.println ("finished.");
	}

	public void open(String recordStoreName, boolean create) throws RecordStoreNotFoundException {

		// Moved from the static block into open() in order to avoid
		// the creation of a rms/ dir although rms is not used in a MIDlet.
	
		//System.out.println("RecordStore.openRecordStore("+recordStoreName+", "+create + ");");
	
		if (refCount++ > 0)
			return;

		this.recordStoreName = recordStoreName;

		if (isApplet()) {
			if (records == null) {
				if (!create) {
					refCount = 0;
                    System.out.println("RMS ERRROR!");
					throw new RecordStoreNotFoundException();
				}

				records = new Vector();
                refCount++;// add extra ref for applets so the stuff is not unloaded
			}
		} else {
            rmsDir.mkdirs();
            
			file = new File(rmsDir, this.recordStoreName + ".rms");

			try {
				DataInputStream dis = new DataInputStream(new FileInputStream(file));

				version = dis.readInt();
				lastModified = dis.readLong();
				int count = dis.readInt();
				records = new Vector();

				for (int i = 0; i < count; i++) {
					int length = dis.readInt();
					if (length >= 0) {
						byte[] buffer = new byte[length];
						dis.readFully(buffer, 0, length);
						records.addElement(buffer);
					} else if (length == -2) {
						records.addElement(RECORD_INVALID);
					} else if (length == -1) {
						records.addElement(null);
					}
				}
				dis.close();
			} catch (Exception ioe) {

				if (!create) {
					refCount = 0;
					throw new RecordStoreNotFoundException();
				}

				records = new Vector();
			}
		}
	}

	public int addRecord(byte[] data, int offset, int numBytes) throws RecordStoreNotOpenException, RecordStoreException, RecordStoreFullException {

		checkOpen();

		if (data == null)
			records.addElement(null);
		else {
			byte[] newData = new byte[numBytes];
			System.arraycopy(data, offset, newData, 0, numBytes);
			records.addElement(newData);
		}

		changeVersion();

		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				((RecordListener) listeners.elementAt(i)).recordAdded(this, records.size());
			}
		}

		writeToFile();

		return records.size();
	}

	public void closeRecordStore() throws RecordStoreNotOpenException, RecordStoreException {

		if (refCount > 0)
			refCount--;

		if (isApplet())
			return;

		writeToFile();
	}

	public void deleteRecordStoreImpl() throws RecordStoreException {
		if (refCount != 1)
			throw new RecordStoreException("RecordStore is open!");

		if (!isApplet()) {
			if (!file.delete())
				throw new RecordStoreException("Cannot delete Store " + file.getName());
		}
	}

	public void deleteRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {

		checkId(recordId);
		records.setElementAt(RECORD_INVALID, recordId - 1);

		writeToFile();

		changeVersion();

		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				((RecordListener) listeners.elementAt(i)).recordDeleted(this, recordId);
			}
		}
	}

	public long getLastModified() throws RecordStoreNotOpenException {
		checkOpen();
		return lastModified;
	}

	public String getName() throws RecordStoreNotOpenException {
		checkOpen();
		return recordStoreName;
	}

	public int getNextRecordID() throws RecordStoreNotOpenException, RecordStoreException {
		checkOpen();
		return records.size() + 1;
	}

	public int getNumRecords() throws RecordStoreNotOpenException {
		checkOpen();
		int result = 0;

		for (int i = 0; i < records.size(); i++) {
			Object data = records.elementAt(i);
			if (data != RECORD_INVALID)
				result++;
		}
		return result;
	}

	public byte[] getRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {

		checkId(recordId);
		byte[] buffer = (byte[]) records.elementAt(recordId - 1);

		if (buffer == RECORD_INVALID)
			throw new InvalidRecordIDException("Record ID " + recordId + " is already deleted");

		return (buffer);
	}

	public int getRecord(int recordId, byte[] buffer, int offset) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException, ArrayIndexOutOfBoundsException {

		byte[] data = getRecord(recordId);
		if (data == RECORD_INVALID)
			throw new InvalidRecordIDException("Record ID " + recordId + " is already deleted.");

		System.arraycopy(data, 0, buffer, offset, data.length);
		return data.length;
	}

	public int getRecordSize(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException {

		return getRecord(recordId).length;
	}

	public int getSize() throws RecordStoreNotOpenException {
		checkOpen();

		int size = 0;

		// version
		size += 4;
		// last modified
		size += 8;

		for (int i = 0; i < records.size(); i++) {
			Object obj = records.elementAt(i);
			// size of buffer
			size += 4;
			// the buffer
			if (obj != null)
				size += ((byte[]) obj).length;

		}

		return size;
	}

	public int getSizeAvailable() throws RecordStoreNotOpenException {
		return Integer.MAX_VALUE;
	}

	public int getVersion() throws RecordStoreNotOpenException {
		checkOpen();
		return version;
	}

	public String[] listRecordStoresImpl() {

		String[] databases;

		if (isApplet()) {
			databases = new String[recordStores.size()];
			int i = 0;
			for (Enumeration e = recordStores.keys(); e.hasMoreElements();)
				databases[i++] = (String) e.nextElement();
		} else {
			File directory = rmsDir;

			databases = directory.isDirectory() ? directory.list(this) : new String[0];
		}

		for (int i = 0; i < databases.length; i++) {
			databases[i] = databases[i].substring(0, databases[i].length() - 4);
		}

		return databases;
	}

	public void setRecord(int recordId, byte[] data, int offset, int numBytes) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException, RecordStoreFullException {

		checkId(recordId);

		byte[] oldData = getRecord(recordId);

		if (oldData == RECORD_INVALID)
			throw new InvalidRecordIDException("Record " + recordId + " is already deleted.");

		if (data != null) {
			byte[] newData = new byte[numBytes];

			System.arraycopy(data, offset, newData, 0, numBytes);

			records.setElementAt(newData, recordId - 1);
		} else {

			records.setElementAt(null, recordId - 1);
		}

		writeToFile();

		changeVersion();

		if (listeners != null) {
			for (int i = 0; i < listeners.size(); i++) {
				((RecordListener) listeners.elementAt(i)).recordChanged(this, recordId);
			}
		}
	}

	public void setMode(int authmode, boolean writable) throws RecordStoreException {
		System.out.println("RecordStore.setMode() called with no effect!");

	}
}

/*
 * $Log: RecordStoreImpl_file.java,v $
 * Revision 1.1.1.1  2006/09/01 17:40:13  thiagolm
 * Initianting the project.
 *
 * Revision 1.14  2002/07/15 22:12:57  mkroll
 * Removed a bug in writeToFile() if isApplet. Added functionality for reading rms.home dynamically for MIDletSuiteManager.
 *
 * Revision 1.13  2002/07/14 22:45:31  haustein
 * applet property access fix, resource access  fixes, rms applet fix
 *
 * Revision 1.12  2002/07/11 22:17:45  mkroll
 * Removed debug output.
 *
 * Revision 1.11  2002/06/06 22:10:33  haustein
 * jdk1.4 compatibility fix, window fix
 *
 * Revision 1.10  2002/04/18 17:08:24  mkroll
 * Records are now stored whenever they are added/changed/deleted.
 *
 * Revision 1.9  2002/02/28 18:34:28  gandre
 * fix in open method for re-opening a record store in an applet
 *
 * Revision 1.8  2002/02/26 00:31:53  haustein
 * adoptions f. siemens support
 *
 * Revision 1.7  2002/02/05 23:15:12  haustein
 * more support for nokia skin added
 *
 * Revision 1.6  2002/01/18 15:50:12  mkroll
 * DataInputStream is now closed after the data is read. Had conflicts with deleteRecordStore()
 *
 * Revision 1.5  2002/01/18 13:58:07  haustein
 * changed deleteRecordStore functionaly back to previously fixed version(cvs update -d -P!)
 *
 * Revision 1.4  2002/01/18 13:25:52  mkroll
 * Added deteled record support and deleteRecordStore()
 *
 */