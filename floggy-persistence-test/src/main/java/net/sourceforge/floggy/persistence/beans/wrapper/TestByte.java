package net.sourceforge.floggy.persistence.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestByte implements Persistable {
    protected Byte x;

    public void setX(Byte x) {
	this.x = x;
    }

    public Byte getX() {
	return x;
    }
}
