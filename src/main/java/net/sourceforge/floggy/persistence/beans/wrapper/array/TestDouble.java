package net.sourceforge.floggy.persistence.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestDouble implements Persistable {
    protected Double[] x;

    public void setX(Double[] x) {
	this.x = x;
    }

    public Double[] getX() {
	return x;
    }
}
