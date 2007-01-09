package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestLong implements Persistable {
    protected long x;

    public void setX(long x) {
	this.x = x;
    }

    public long getX() {
	return x;
    }
}
