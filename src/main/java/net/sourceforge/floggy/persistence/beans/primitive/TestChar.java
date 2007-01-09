package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestChar implements Persistable {
    protected char x;

    public void setX(char x) {
	this.x = x;
    }

    public char getX() {
	return x;
    }
}
