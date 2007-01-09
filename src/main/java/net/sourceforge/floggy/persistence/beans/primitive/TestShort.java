package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestShort implements Persistable {
    protected short x;

    public void setX(short x) {
	this.x = x;
    }

    public short getX() {
	return x;
    }
}
