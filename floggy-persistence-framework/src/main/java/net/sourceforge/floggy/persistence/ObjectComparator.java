/*
 * Created in 21/08/2005.
 *
 * All rights reserved to the authors.
 */
package net.sourceforge.floggy.persistence;

import javax.microedition.rms.RecordComparator;

/**
 * An implementation of RecordComparator for comparing two objects.
 * 
 * @author Thiago Rossato
 * @since 1.0
 */
class ObjectComparator implements RecordComparator {

    private Comparator comparator;

    private Persistable o1;

    private Persistable o2;

    ObjectComparator(Class persistableClass, Comparator comparator)
	    throws FloggyException {

	try {
	    this.o1 = (Persistable) persistableClass.newInstance();
	    this.o2 = (Persistable) persistableClass.newInstance();
	} catch (Exception e) {
	    throw new FloggyException(e.getMessage());
	}
	this.comparator = comparator;
    }

    public int compare(byte[] buffer1, byte[] buffer2) {
	try {
	    ((__Persistable) o1).__load(buffer1);
	    ((__Persistable) o2).__load(buffer2);
	} catch (Exception e) {
	    // Ignore
	}

	int result = comparator.compare(o1, o2);
	switch (result) {
	case Comparator.PRECEDES:
	    result = RecordComparator.PRECEDES;
	    break;
	case Comparator.EQUIVALENT:
	    result = RecordComparator.EQUIVALENT;
	    break;
	case Comparator.FOLLOWS:
	    result = RecordComparator.FOLLOWS;
	    break;
	}
	return result;
    }
}
