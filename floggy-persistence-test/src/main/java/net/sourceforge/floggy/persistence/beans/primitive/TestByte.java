package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestByte implements Persistable {
    protected byte x;

    public void setX(byte x) {
	this.x = x;
    }

    public byte getX() {
	return x;
    }
}
