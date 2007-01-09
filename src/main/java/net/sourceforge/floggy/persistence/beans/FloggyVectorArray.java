package net.sourceforge.floggy.persistence.beans;

import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;

public class FloggyVectorArray implements Persistable {

    private Vector[] x;

    public Vector[] getX() {
	return x;
    }

    public void setX(Vector[] x) {
	this.x = x;
    }

}
