package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

public class FloggyStaticAttribute implements Persistable {

    public static Object x = null;

    public Object getX() {
	return FloggyStaticAttribute.x;
    }

    public void setX(Object x) {
	FloggyStaticAttribute.x = x;
    }

}
