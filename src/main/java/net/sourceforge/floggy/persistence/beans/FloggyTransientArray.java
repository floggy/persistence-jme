package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

public class FloggyTransientArray implements Persistable {

    private transient Object[] x;

    public Object[] getX() {
	return x;
    }

    public void setX(Object[] x) {
	this.x = x;
    }

    public FloggyTransientArray() {
	super();
    }

}
