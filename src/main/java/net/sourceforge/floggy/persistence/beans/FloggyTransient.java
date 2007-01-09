package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

public class FloggyTransient implements Persistable {

    private transient Object x;

    public Object getX() {
	return x;
    }

    public void setX(Object x) {
	this.x = x;
    }

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
	FloggyTransient castedObj = (FloggyTransient) o;
	return ((this.x == null ? castedObj.x == null : this.x
		.equals(castedObj.x)));
    }

}
