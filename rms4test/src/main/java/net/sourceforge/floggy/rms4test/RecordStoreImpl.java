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

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;


public abstract class RecordStoreImpl extends RecordStore {

    public static Hashtable recordStores = new Hashtable ();
    public static RecordStoreImpl metaStore = newInstance ();
    
   
    protected Vector listeners;
    
    String recordStoreName;
    int refCount;


    public void addRecordListener (RecordListener listener) {
	if (listeners == null)
	    listeners = new Vector ();
	
	listeners.insertElementAt(listener , listeners.size());
    }

    
    public void removeRecordListener (RecordListener listener) {
	if (listeners != null)
	    listeners.removeElement (listener);
    }
    
       
    public abstract void deleteRecordStoreImpl () throws RecordStoreException ;


    void checkOpen () throws RecordStoreNotOpenException {
	if (refCount == 0) 
	    throw new RecordStoreNotOpenException 
		("RecordStore not open: "+recordStoreName);
    } 


    void checkId (int index) throws RecordStoreException {
	checkOpen ();
	if (index < 1 || index >= getNextRecordID ()) 
	    throw new InvalidRecordIDException 
		("Id "+index+" not valid in recordstore "+recordStoreName);
    }


    public static RecordStoreImpl newInstance () {
           return new RecordStoreImpl_file ();
    }

    
    public abstract String [] listRecordStoresImpl ();

    public abstract void open (String recordStoreName, 
			       boolean create) throws RecordStoreNotFoundException;
}

/*
 * $Log: RecordStoreImpl.java,v $
 * Revision 1.1.1.1  2006/09/01 17:40:13  thiagolm
 * Initianting the project.
 *
 * Revision 1.9  2002/04/18 18:16:52  mkroll
 * Removed <boolean file> from createInstance
 *
 * Revision 1.8  2002/04/18 17:14:16  mkroll
 * Added keepUpdated functionality
 *
 * Revision 1.7  2002/04/18 14:59:58  haustein
 * JDK 1.1 incompatibilities fiexed
 *
 * Revision 1.6  2002/01/18 13:58:07  haustein
 * changed deleteRecordStore functionaly back to previously fixed version(cvs update -d -P!)
 *
 * Revision 1.5  2002/01/18 13:25:00  mkroll
 * Added deteled record support and deleteRecordStore()
 *
 */
