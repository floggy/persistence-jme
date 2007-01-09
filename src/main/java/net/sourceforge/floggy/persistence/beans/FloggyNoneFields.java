package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class FloggyNoneFields implements Persistable {

    /**
         * Returns <code>true</code> if this <code>FloggyTransient</code> is
         * the same as the o argument.
         * 
         * @return <code>true</code> if this <code>FloggyTransient</code> is
         *         the same as the o argument.
         */
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (o == null) {
	    return false;
	}
	if (o.getClass() != getClass()) {
	    return false;
	}
	return true;
    }

}
