package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

public class FloggyStaticAttributeArray implements Persistable {

    public static Object[] x;

    public Object[] getX() {
	return x;
    }

    public void setX(Object[] x) {
	FloggyStaticAttributeArray.x = x;
    }

}
