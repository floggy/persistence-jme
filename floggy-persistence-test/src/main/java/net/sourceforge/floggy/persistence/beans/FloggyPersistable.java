package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class FloggyPersistable implements Persistable {
    protected FloggyPersistable x;

    public void setX(FloggyPersistable x) {
	this.x = x;
    }

    public FloggyPersistable getX() {
	return x;
    }

    /**
         * Returns <code>true</code> if this <code>FloggyPersistable</code>
         * is the same as the o argument.
         * 
         * @return <code>true</code> if this <code>FloggyPersistable</code>
         *         is the same as the o argument.
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
	FloggyPersistable castedObj = (FloggyPersistable) o;
	return ((this.x == null ? castedObj.x == null : this.x
		.equals(castedObj.x)));
    }
}
