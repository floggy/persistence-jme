package net.sourceforge.floggy.persistence.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class FloggyBoolean implements Persistable {
    protected Boolean[] x;

    public Boolean[] getX() {
	return x;
    }

    public void setX(Boolean[] x) {
	this.x = x;
    }
}
