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

package javax.microedition.rms;

import net.sourceforge.floggy.rms4test.RecordEnumerationImpl;
import net.sourceforge.floggy.rms4test.RecordStoreImpl;

/**
 * A class representing a record store. A record store consists of a 
 * collection of records which will remain persistent across multiple 
 * invocations of the MIDlet. The platform is responsible for making its 
 * best effort to maintain the integrity of the MIDlet's record stores 
 * throughout the normal use of the platform, including reboots, battery 
 * changes, etc. Record stores are created in platform-dependent locations, 
 * which are not exposed to the MIDlets. The naming space for record stores 
 * is controlled at the MIDlet suite granularity. MIDlets within a MIDlet 
 * suite are allowed to create multiple record stores, as long as they are 
 * each given different names. When a MIDlet suite is removed from a platform 
 * all the record stores associated with its MIDlets will also be removed. 
 * MIDlets within a MIDlet suite can access each other's record stores directly. 
 * 
 * New APIs in MIDP 2.0 allow for the explicit sharing of record stores if the 
 * MIDlet creating the RecordStore chooses to give such permission.
 * 
 * Sharing is accomplished through the ability to name a RecordStore created by 
 * another MIDlet suite. RecordStores are uniquely named using the unique name 
 * of the MIDlet suite plus the name of the RecordStore. MIDlet suites are 
 * identified by the MIDlet-Vendor and MIDlet-Name attributes from the application 
 * descriptor. Access controls are defined when RecordStores to be shared are 
 * created. Access controls are enforced when RecordStores are opened. The 
 * access modes allow private use or shareable with any other MIDlet suite.
 * Record store names are case sensitive and may consist of any combination of 
 * between one and 32 Unicode characters inclusive. Record store names must 
 * be unique within the scope of a given MIDlet suite. In other words, MIDlets 
 * within a MIDlet suite are not allowed to create more than one record store 
 * with the same name, however a MIDlet in one MIDlet suite is allowed to have 
 * a record store with the same name as a MIDlet in another MIDlet suite. In 
 * that case, the record stores are still distinct and separate.
 * No locking operations are provided in this API. Record store implementations 
 * ensure that all individual record store operations are atomic, synchronous, 
 * and serialized, so no corruption will occur with multiple accesses. 
 * However, if a MIDlet uses multiple threads to access a record store, 
 * it is the MIDlet's responsibility to coordinate this access or unintended 
 * consequences may result. Similarly, if a platform performs transparent 
 * synchronization of a record store, it is the platform's responsibility to 
 * enforce exclusive access to the record store between the MIDlet and 
 * synchronization engine. Records are uniquely identified within a given record 
 * store by their recordId, which is an integer value. This recordId is used 
 * as the primary key for the records. The first record created in a record store 
 * will have recordId equal to one (1). Each subsequent record added to a 
 * RecordStore will be assigned a recordId one greater than the record added 
 * before it. That is, if two records are added to a record store, and the first 
 * has a recordId of 'n', the next will have a recordId of 'n + 1'. MIDlets 
 * can create other sequences of the records in the RecordStore by using 
 * the RecordEnumeration class. This record store uses long integers for 
 * time/date stamps, in the format used by System.currentTimeMillis(). 
 * The record store is time stamped with the last time it was modified. 
 * The record store also maintains a version number, which is an integer 
 * that is incremented for each operation that modifies the contents of 
 * the RecordStore. These are useful for synchronization engines as well 
 * as other things.
 *
 * @API MIDP-1.0 
 * @API MIDP-2.0
 */
public abstract class RecordStore {

    public static final int AUTHMODE_PRIVATE = 0;
    public static final int AUTHMODE_ANY = 1;
    
    
	/**
	 * @ME4SE INTERNAL
	 */
	protected RecordStore() {
	}

	/** 
	 * Adds a new record to the record store.
	 * @param data The data to be stored in this record. If the
	 *             record should have zero-length - no data - this
	 *             parameter may be set to null
	 * @param offset The index of the data buffer of the first
	 *               databyte for this record
	 * @return The recordid of the new record
	 * @exception RecordStoreNotOpenException if the record store is
	 *            not yet open.
	 * @exception InvalidRecordIDException if the recordId is invalid.
	 * @exception RecordStoreException if a general
	 *            recordstoreexception occurs.  
	 * 
	 * @API MIDP-1.0
	 */
	public abstract int addRecord(byte[] data, int offset, int count) throws RecordStoreNotOpenException, RecordStoreException, RecordStoreFullException;

	/**
	 * Adds the specified RecordListener.
	 * @param listener The RecordChangedListener
	 *
	 * @API MIDP-1.0
	 */
	public abstract void addRecordListener(RecordListener listener);

	/** 
	 * This method is called when the MIDlet requests to have the
	 * record store closed.
	 *
	 * @exception RecordStoreNotOpenException if the record store is
	 *            not open
	 * @exception RecordStoreException if another record store related
	 *            Exception occurs 
	 *
	 * @API MIDP-1.0
	 */

	public abstract void closeRecordStore() throws RecordStoreNotOpenException, RecordStoreException;

	/** 
	 * The record is deleted from the record store.
	 * @param recordId The ID of the record to be deleted
	 * @exception RecordStoreNotOpenException if the record store is not open
	 * @exception RecordStoreException if a different record store-related exception occurs 
	 * 
	 * @API MIDP-1.0 
	 */

	public abstract void deleteRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;

	/** 
	 * Deletes the named record store.
	 * @param recordStoreName The record store to be deleted
	 * @exception RecordStoreNotFoundException if the record store
	 *            could not be found.
	 * @exception RecordStoreException if a record store related
	 *            exception occurs.  
	 * 
	 * @API MIDP-1.0
	 */
	public static void deleteRecordStore(String recordStoreName) throws RecordStoreException, RecordStoreNotFoundException {
		((RecordStoreImpl) openRecordStore(recordStoreName, false)).deleteRecordStoreImpl();
		RecordStoreImpl.recordStores.remove(recordStoreName);
	}

	/**
	 * Returns an enumeration for traversing a set of records in the record store 
	 * in an optionally specified order. The filter, if non-null, will be used to 
	 * determine what subset of the record store records will be used.
	 * The comparator, if non-null, will be used to determine the order in which 
	 * the records are returned. If both the filter and comparator is null, the 
	 * enumeration will traverse all records in the record store in an undefined 
	 * order. This is the most efficient way to traverse all of the records 
	 * in a record store. If a filter is used with a null comparator, 
	 * the enumeration will traverse the filtered records in an undefined order. 
	 * The first call to RecordEnumeration.nextRecord() returns the record data 
	 * from the first record in the sequence. Subsequent calls to 
	 * RecordEnumeration.nextRecord() return the next consecutive record's data. 
	 * To return the record data from the previous consecutive from any given point 
	 * in the enumeration, call previousRecord(). On the other hand, if after 
	 * creation the first call is to previousRecord(), the record data of the 
	 * last element of the enumeration will be returned. Each subsequent call to 
	 * previousRecord() will step backwards through the sequence.
	 * @param filter if non-null, will be used to determine what subset of the 
	 *               record store records will be used
	 * @param comparator if non-null, will be used to determine the order in 
	 *                   which the records are returned
	 * @param keepUpdated if true, the enumerator will keep its enumeration 
	 *                    current with any changes in the records of the record 
	 *                    store. Use with caution as there are possible performance 
	 *                    consequences. If false the enumeration will not be kept 
	 *                    current and may return recordIds for records that 
	 *                    have been deleted or miss records that are added later. 
	 *                    It may also return records out of order that have been 
	 *                    modified after the enumeration was built. Note that any 
	 *                    changes to records in the record store are accurately 
	 *                    reflected when the record is later retrieved, either 
	 *                    directly or through the enumeration. The thing that is 
	 *                    risked by setting this parameter false is the filtering 
	 *                    and sorting order of the enumeration when records are 
	 *                    modified, added, or deleted.
	 * @return an enumeration for traversing a set of records in the record store 
	 *         in an optionally specified order
	 * @throws RecordStoreNotOpenException if the record store is not open
	 *
	 * @API MIDP-1.0	 
	 */
	public RecordEnumeration enumerateRecords(RecordFilter filter, RecordComparator comparator, boolean keepUpdated) throws RecordStoreNotOpenException {
		return new RecordEnumerationImpl((RecordStoreImpl) this, filter, comparator, keepUpdated);
	}

	/** 
	 * Returns the last time the record store was modified, in the
	 * format used by System.currentTimeMillis().
	 * @return the last time the record store was modified, in the same
	 *         format used by System.currentTimeMillis ();
	 * @exception RecordStoreNotOpenException if the record store is not open. 
	 * 
	 * @API MIDP-1.0 
	 */

	public abstract long getLastModified() throws RecordStoreNotOpenException;

	/**
	 * Returns the name of this RecordStore.
	 * 
	 * @return the name of this record store
	 * @exception RecordStoreNotOpenException if the record store is not open.
	 *
	 * @API MIDP-1.0
	 */
	public abstract String getName() throws RecordStoreNotOpenException;

	/**
	 * Returns the recordId of the next record to be added to the record store.
	 * 
	 * @return the recordId of the record store to be added to this record store.
	 * @exception RecordStoreNotOpenException if the record store is not open.
	 * @exception RecordStoreException if another record store related exception occurs
	 * 
	 * @API MIDP-1.0
	 */
	public abstract int getNextRecordID() throws RecordStoreNotOpenException, RecordStoreException;

	/**
	 * Returns the number of records currently in the record store.
	 *
	 * @return the number of records currently in the opened record store
	 * @exception RecordStoreNotOpenException if the record store is not open.
	 * 
	 * @API MIDP-1.0
	 */
	public abstract int getNumRecords() throws RecordStoreNotOpenException;

	/**
	 * Returns a copy of the data stored in the given record.
	 *
	 * @param recordId The ID of the record to use in this operation
	 * @exception RecordStoreNotOpenException if the record store is not open.
	 * @exception InvalidRecordIDException if the record store is invalid.
	 * @exception RecordStoreException if a general record store exception occurs.
	 * 
	 * @API MIDP-1.0
	 */
	public abstract byte[] getRecord(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;

	/** 
	 * Returns the data stored in the given record.
	 *
	 * @param recordId The ID of the record to be used in this
	 *                 operation.
	 * @param buffer The byte array to copy the data.
	 * @param offset The index index into the buffer i which to start copiying.
	 * @return the number of bytes copied into the buffer, startting at
	 *         index offset
	 * @exception RecordStoreNotOpenException if the record store is
	 *            not open.
	 * @exception InvalidRecordIDException if the record store is
	 *            invalid.
	 * @exception RecordStoreException if a general record store
	 *            exception occurs.
	 * @exception ArrayIndexOutOfBoundsException if the record is larger 
	 *            that the buffer supplied
	 * 
	 * @API MIDP-1.0 
	 */
	public abstract int getRecord(int recordId, byte[] buffer, int offset) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException, ArrayIndexOutOfBoundsException;

	/**	
	 * Returns the size (in bytes) of the application data available in the given record.
	 * 
	 * @param recordId The ID of the record to be used in this operation.
	 * @return the size in bytes of the data available in the record
	 * @exception RecordStoreNotOpenException if the record store is not open.
	 * @exception InvalidRecordIDException if the record store is invalid.
	 * @exception RecordStoreException if a general record store exception occurs.
	 * 
	 * @API MIDP-1.0
	 */
	public abstract int getRecordSize(int recordId) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException;

	/**
	 * Returns the amount of space, in bytes, that the record store occupies.
	 * 
	 * @return the size of the record store in bytes 
	 * @exception RecordStoreNotOpenException if the record store is not open.
	 * 
	 * @API MIDP-1.0
	 */
	public abstract int getSize() throws RecordStoreNotOpenException;

	/**
	 * Returns the amount of additional room (in bytes) available for this record store to grow.
	 * 
	 * @return The amount of room which is available for the RecordStore to grow.
	 * 
	 * @API MIDP-1.0
	 */
	public abstract int getSizeAvailable() throws RecordStoreNotOpenException;

	/**
	 * Each time a record store is modified (record added, modified, deleted), 
	 * it's version is incremented.
	 * 
	 * @return The version number of the RecordStore
	 * 
	 * @API MIDP-1.0
	 */
	public abstract int getVersion() throws RecordStoreNotOpenException;

	/**
	 * Returns an arry of names of record stores owned by the application if the stores are private.
	 * Note that the function returns NULL if the application does not have any record stores.
	 *
	 * @return an array of the names of record stores.
	 * 
	 * @API MIDP-1.0
	 */
	public static String[] listRecordStores() {

		return RecordStoreImpl.metaStore.listRecordStoresImpl();

	}

	/**
	 * Open (and possibly create) a record store. If this record store is already opened, this method
	 * returns a reference to the same RecordStore object.
	 *
	 * @param recordStoreName The unique name, not to exceed 32 characters, of the record store.
	 * @param createIfNecesarry If true, the record store will be created if necessary.
	 * @return The <code>RecordStore</code> object for the record store.
	 * @exception RecordStoreException if a record store-related exception occurs.
	 * @exception RecordStoreNotFoundException if the record store could not be found
	 *
	 * @API MIDP-1.0
	 */
	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {

		RecordStoreImpl store = (RecordStoreImpl) RecordStoreImpl.recordStores.get(recordStoreName);

		if (store == null) {
			store = RecordStoreImpl.newInstance();
			RecordStoreImpl.recordStores.put(recordStoreName, store);
		}

		store.open(recordStoreName, createIfNecessary);
		return store;
	}

	/**
	 * Open (and possibly create) a record store that can be shared with other MIDlet 
	 * suites. The RecordStore is owned by the current MIDlet suite. The authorization 
	 * mode is set when the record store is created, as follows: 
	 * AUTHMODE_PRIVATE - Only allows the MIDlet suite that created the RecordStore 
	 *                    to access it. This case behaves identically to 
	 *                    openRecordStore(recordStoreName, createIfNecessary). 
	 * AUTHMODE_ANY - Allows any MIDlet to access the RecordStore. Note that this 
	 *                makes your recordStore accessible by any other MIDlet on the device. 
	 *                This could have privacy and security issues depending on the data 
	 *                being shared. Please use carefully. 
	 * 
	 * The owning MIDlet suite may always access the RecordStore and always has access 
	 * to write and update the store. If this method is called by a MIDlet when the record 
	 * store is already open by a MIDlet in the MIDlet suite, this method returns a reference 
	 * to the same RecordStore object.
	 * 
	 * @param recordStoreName the MIDlet suite unique name for the record store, 
	 *                        consisting of between one and 32 Unicode characters inclusive.
	 * @param createIfNecessary if true, the record store will be created if necessary
	 * @param authmode the mode under which to check or create access. Must be one of 
	 *                 AUTHMODE_PRIVATE or AUTHMODE_ANY. This argument is ignored if 
	 *                 the RecordStore exists.
	 * @param writable true if the RecordStore is to be writable by other MIDlet suites 
	 *                 that are granted access. This argument is ignored if the RecordStore exists.
	 * @return RecordStore object for the record store
	 * @throws RecordStoreException if a record store-related exception occurred
	 * @throws RecordStoreNotFoundException if the record store could not be found
	 * @throws RecordStoreFullException if the operation cannot be completed because the 
	 *                                  record store is full
	 * @throws IllegalArgumentException if authmode or recordStoreName is invalid
	 * 
	 * @API MIDP-2.0
	 * @ME4SE UNIMPLEMENTED
	 */
	public static RecordStore openRecordStore(String recordStoreName, boolean createIfNecessary, int authmode, boolean writable) throws RecordStoreException, RecordStoreFullException, RecordStoreNotFoundException {
		System.out.println("RecordStore.openRecordStore(authmode, wriable) called with no effect!");
		return openRecordStore(recordStoreName, createIfNecessary);
	}

	/**
	 * Open a record store associated with the named MIDlet suite. The MIDlet 
	 * suite is identified by MIDlet vendor and MIDlet name. Access is granted only 
	 * if the authorization mode of the RecordStore allows access by the current 
	 * MIDlet suite. Access is limited by the authorization mode set when the record 
	 * store was created: 
	 * AUTHMODE_PRIVATE - Succeeds only if vendorName and suiteName identify the 
	 *                    current MIDlet suite; this case behaves identically to 
	 *                    openRecordStore(recordStoreName, createIfNecessary). 
	 * AUTHMODE_ANY - Always succeeds. Note that this makes your recordStore accessible 
	 *                by any other MIDlet on the device. This could have privacy and 
	 *                security issues depending on the data being shared. Please use 
	 *                carefully. Untrusted MIDlet suites are allowed to share data but 
	 *                this is not recommended. The authenticity of the origin of untrusted 
	 *                MIDlet suites cannot be verified so shared data may be used unscrupulously. 
	 * 
	 * If this method is called by a MIDlet when the record store is already open by a MIDlet 
	 * in the MIDlet suite, this method returns a reference to the same RecordStore object.
	 * 
  	 * If a MIDlet calls this method to open a record store from its own suite, the behavior is 
  	 * identical to calling: openRecordStore(recordStoreName, false)
	 * @param recordStoreName the MIDlet suite unique name for the record store, consisting 
	 *                        of between one and 32 Unicode characters inclusive.
	 * @param vendorName the vendor of the owning MIDlet suite
	 * @param suiteName the name of the MIDlet suite
	 * @return RecordStore object for the record store
	 * @throws RecordStoreException if a record store-related exception occurred
	 * @throws RecordStoreNotFoundException if the record store could not be found
	 * @throws SecurityException if this MIDlet Suite is not allowed to open the 
	 *                           specified RecordStore.
	 * @throws IllegalArgumentException if recordStoreName is invalid
	 * 
	 * @API MIDP-2.0
	 * @ME4SE UNIMPLEMENTED
	 */
	public static RecordStore openRecordStore(String recordStoreName, String vendorName, String suiteName) throws RecordStoreException, RecordStoreNotFoundException {
		System.out.println("RecordStore.openRecordStore(vendorName, suiteName) called with no effect!");
		return openRecordStore(recordStoreName, false);
	}

	/**
	 * Removes the specified RecordListener.
	 * 
	 * @param listener the RecordChangedListener
	 * 
	 * @API MIDP-1.0
	 */
	public abstract void removeRecordListener(RecordListener listener);

	/**
	 * Sets the data in the given record to that passed in.
	 * 
	 * @param recordId The ID of the record store.
	 * @param newdata The new data buffer to store in the record.
	 * @param offset The index into the data buffer of the first new byte to be stored in the record.
	 * @param count The number of bytes of the data buffer to use for this record.
	 * 
	 * @API MIDP-1.0
	 */
	public abstract void setRecord(int recordId, byte[] newData, int offset, int count) throws RecordStoreNotOpenException, InvalidRecordIDException, RecordStoreException, RecordStoreFullException;

	/**
	 * Changes the access mode for this RecordStore. The authorization mode choices are:
	 * 
	 * AUTHMODE_PRIVATE - Only allows the MIDlet suite that created the RecordStore 
	 *                    to access it. This case behaves identically to 
	 *                    openRecordStore(recordStoreName, createIfNecessary). 
	 * AUTHMODE_ANY - Allows any MIDlet to access the RecordStore. Note that this makes 
	 *                your recordStore accessible by any other MIDlet on the device. This 
	 *                could have privacy and security issues depending on the data being shared. 
	 *                Please use carefully.	The owning MIDlet suite may always access the RecordStore 
	 *                and always has access to write and update the store. Only the owning MIDlet 
	 *                suite can change the mode of a RecordStore.
	 * 
	 * @param authmode the mode under which to check or create access. Must be one 
	 *                 of AUTHMODE_PRIVATE or AUTHMODE_ANY.
	 * @param writable - true if the RecordStore is to be writable by other MIDlet suites that are granted access
	 * Throws:
	 * RecordStoreException - if a record store-related exception occurred
	 * SecurityException - if this MIDlet Suite is not allowed to change the mode of the RecordStore
	 * IllegalArgumentException - if authmode is invalid
	 * Since: 
	 * @API MIDP-2.0 
	 * @ME4SE UNIMPLEMENTED
	 */
	public abstract void setMode(int authmode, boolean writable) throws RecordStoreException;

}

/*
 * $Log: RecordStore.java,v $
 * Revision 1.1.1.1  2006/09/01 17:40:13  thiagolm
 * Initianting the project.
 *
 * Revision 1.19  2005/01/16 21:29:25  haustein
 * synced with fixes for siemens
 *
 * Revision 1.18  2003/11/08 15:01:23  mkroll
 * added api tags
 *
 * Revision 1.17  2003/11/07 23:41:03  mkroll
 * changed runtimeexceptions in ne midp2 stuff to simple system outs.
 *
 * Revision 1.16  2003/11/07 21:45:46  mkroll
 * *** empty log message ***
 *
 * Revision 1.15  2003/11/07 21:27:35  mkroll
 * *** empty log message ***
 *
 * Revision 1.14  2003/11/07 18:04:04  mkroll
 * added new API tags for JavaDoc generation
 *
 * Revision 1.13  2003/11/07 17:38:53  mkroll
 * added new API tags for JavaDoc generation
 *
 * Revision 1.12  2003/01/30 19:02:49  haustein
 * deleted sources restored from local history
 *
 * Revision 1.10  2002/12/05 23:30:25  mkroll
 * Removed some unneccessary imports.
 *
 * Revision 1.9  2002/07/15 22:56:24  mkroll
 * Added last fixes after moving CommConnection to org.me4se.impl.gcf
 *
 * Revision 1.8  2002/07/15 22:46:52  haustein
 * moved jad and chooser back, moved rms and gcf stuff to appropriate subpackages
 *
 * Revision 1.7  2002/06/17 11:13:55  haustein
 * rms listrecordstore exception removed
 *
 * Revision 1.6  2002/05/31 19:42:51  haustein
 * recordstoreexception added for listrecordstores
 *
 * Revision 1.5  2002/05/31 19:19:22  haustein
 * gcf preparations for file/serial
 *
 * Revision 1.4  2002/01/18 13:58:02  haustein
 * changed deleteRecordStore functionaly back to previously fixed version(cvs update -d -P!)
 *
 * Revision 1.3  2002/01/18 13:22:14  mkroll
 * Added deteled record support and deleteRecordStore()
 *
 */