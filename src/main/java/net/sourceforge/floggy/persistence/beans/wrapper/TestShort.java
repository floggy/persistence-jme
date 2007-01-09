package net.sourceforge.floggy.persistence.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestShort implements Persistable {
    protected Short x;

    public void setX(Short x) {
	this.x = x;
    }

    public Short getX() {
	return x;
    }
}
