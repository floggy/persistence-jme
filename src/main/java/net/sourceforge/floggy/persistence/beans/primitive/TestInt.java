package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestInt implements Persistable {
    private int x;

    public int getX() {
	return x;
    }

    public void setX(int x) {
	this.x = x;
    }
}
