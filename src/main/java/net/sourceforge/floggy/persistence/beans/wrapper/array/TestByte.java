package net.sourceforge.floggy.persistence.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Le�o Moreira <thiagolm@users.sourceforge.net>
 */
public class TestByte implements Persistable {
    protected Byte[] x;

    public void setX(Byte[] x) {
	this.x = x;
    }

    public Byte[] getX() {
	return x;
    }
}
