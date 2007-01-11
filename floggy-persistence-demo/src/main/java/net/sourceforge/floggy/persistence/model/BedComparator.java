package net.sourceforge.floggy.persistence.model;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.Persistable;

public class BedComparator implements Comparator {

	public int compare(Persistable arg0, Persistable arg1) {
        Bed b1 = (Bed) arg0;
        Bed b2 = (Bed) arg1;
        if (b1.getNumber() == b2.getNumber()) {
        	return Comparator.EQUIVALENT;
        } else if (b1.getNumber() > b2.getNumber()) {
            return Comparator.FOLLOWS;
        } else {
            return Comparator.PRECEDES;
        }
	}

}
