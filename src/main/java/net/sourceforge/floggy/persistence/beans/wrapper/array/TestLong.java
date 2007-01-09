package net.sourceforge.floggy.persistence.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Le�o Moreira <thiagolm@users.sourceforge.net>
 */
public class TestLong implements Persistable {
    protected Long[] x;

    public void setX(Long[] x) {
	this.x = x;
    }

    public Long[] getX() {
	return x;
    }
}
