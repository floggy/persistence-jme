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
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 * @author Thiago Rossato <thiagorossato@users.sourceforge.net>
 * @since 1.0
 */
class ObjectComparator implements RecordComparator {

	private Comparator comparator;

	private final Persistable p1;

	private final Persistable p2;

	ObjectComparator(Comparator comparator, Persistable p1, Persistable p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.comparator = comparator;
	}

	public int compare(byte[] buffer1, byte[] buffer2) {
		try {
			((__Persistable) p1).__load(buffer1);
			((__Persistable) p2).__load(buffer2);
		} catch (Exception e) {
			// Ignore
		}

		int result = comparator.compare(p1, p2);
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
