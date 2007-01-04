/*
 *  ME4SE - A MicroEdition Emulation for J2SE 
 *
 * Copyright (C) 2001 Stefan Haustein, Oberhausen (Rhld.), Germany
 *
 * Contributors: Oleg Oksyuk
 *				 Michael Kroll
 * 
 *
 * STATUS: 
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version. This program is
 * distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public
 * License for more details. You should have received a copy of the
 * GNU General Public License along with this program; if not, write
 * to the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * $Id: RecordEnumerationImpl.java,v 1.1.1.1 2006/09/01 17:40:13 thiagolm Exp $
 */
package net.sourceforge.floggy.rms4test;

import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * 
 */
public class RecordEnumerationImpl
	implements RecordEnumeration, RecordListener {

	RecordStoreImpl store;
	RecordFilter filter;
	RecordComparator comparator;
	boolean keepUpdated;

	Vector ids = new Vector();
	int currentIndex = -1;
	int indexCnt = 0;


	public RecordEnumerationImpl(
		RecordStoreImpl store,
		RecordFilter filter,
		RecordComparator comparator,
		boolean keepUpdated)
		throws RecordStoreNotOpenException {

		store.checkOpen();
		this.store = store;
		this.filter = filter;
		this.comparator = comparator;
		this.keepUpdated = keepUpdated;

		loadRecords();

		if (keepUpdated)
			store.addRecordListener(this);
	}


	public void destroy() {
	}

	public boolean hasNextElement() {
		return (currentIndex < indexCnt - 1);
	}

	public boolean hasPreviousElement() {
		return (currentIndex >= 0);
	}

	public boolean isKeptUpdated() {
		return keepUpdated;
	}

	public void keepUpdated(boolean keepUpdated) {
		this.keepUpdated = keepUpdated;
		if (keepUpdated) {
			store.addRecordListener(this);
		} else {
			store.removeRecordListener(this);
		}
	}

	public byte[] nextRecord() throws RecordStoreException {
		return store.getRecord(nextRecordId());
	}

	public int nextRecordId() throws InvalidRecordIDException {
		if (!hasNextElement())
			throw new InvalidRecordIDException();
		Integer id = (Integer) ids.elementAt(++currentIndex);

		return id.intValue();
	}

	public int numRecords() {
		return indexCnt;
	}

	public byte[] previousRecord() throws RecordStoreException {
		return store.getRecord(previousRecordId());
	}

	public int previousRecordId() throws InvalidRecordIDException {
		if (!hasPreviousElement())
			throw new InvalidRecordIDException();
		Integer id = (Integer) ids.elementAt(--currentIndex);
		return id.intValue();
	}

	public void rebuild() {
		loadRecords();
	}

	public void reset() {
		currentIndex = -1;
	}

	private byte[] getRecord(int index) throws RecordStoreException {
		return store.getRecord(((Integer) ids.elementAt(index)).intValue());
	}

	private void loadRecords() {
		indexCnt = 0;
		ids = new Vector();
		int id = -1;
		int j = -1;
		int n = -1;

		//		try {
		byte[] record = null;
		byte[] tmpRecord = null;
		try {
			n = store.getNextRecordID();
		} catch (RecordStoreException e) {
		}
		//recordid = 1;
		boolean added;

		// we should replace this by a binary search.....
		// (or build a sorted vector-alike in org.kobjects.?)
		for (id = 1; id < n; id++) {
			try {
				record = store.getRecord(id);
				if ((record != null) && ((filter == null) || filter.matches(record))) {
					if (comparator != null) {
						added = false;
						for (j = 0; j < indexCnt; j++) {
							try {
								tmpRecord = getRecord(j);
								if (comparator.compare(record, tmpRecord) != RecordComparator.FOLLOWS) {
									//records.insertElementAt (tmpRecord, j);
									ids.insertElementAt(new Integer(id), j);
									added = true;
									break;
								}
							} catch (RecordStoreException e) {
							}
						}
						if (!added) {
							//records.addElement (record);
							ids.addElement(new Integer(id));
						}
					} else {
						//records.addElement(record);
						ids.addElement(new Integer(id));
					}
					++indexCnt;
				}

			} catch (RecordStoreException e) {
			}
		}
		indexCnt = ids.size();
		//		} catch (RecordStoreException e) {
		//			throw new RuntimeException(e.toString());
		//		}
	}

	public void recordAdded(RecordStore recordStore, int recordId) {
		loadRecords();
	}

	public void recordChanged(RecordStore recordStore, int recordId) {
		loadRecords();
	}

	public void recordDeleted(RecordStore recordStore, int recordId) {
		loadRecords();
	}
}

/*
 * $Log: RecordEnumerationImpl.java,v $
 * Revision 1.1.1.1  2006/09/01 17:40:13  thiagolm
 * Initianting the project.
 *
 * Revision 1.8  2002/06/06 22:10:33  haustein
 * jdk1.4 compatibility fix, window fix
 *
 * Revision 1.7  2002/04/28 22:44:08  mkroll
 * Fixed a bug with deleted records.
 *
 * Revision 1.6  2002/04/18 17:14:16  mkroll
 * Added keepUpdated functionality
 *
 */