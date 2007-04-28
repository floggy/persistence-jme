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

	private final Comparator c;

	private final Persistable p1;

	private final Persistable p2;

	ObjectComparator(Comparator c, Persistable p1, Persistable p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.c = c;
	}

	public int compare(byte[] b1, byte[] b2) {
		try {
			((__Persistable) p1).__load(b1);
			((__Persistable) p2).__load(b2);
		} catch (Exception e) {
			// Ignore
		}

		return c.compare(p1, p2);
	}
}
