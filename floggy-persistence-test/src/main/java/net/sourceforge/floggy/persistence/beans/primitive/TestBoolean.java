package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestBoolean implements Persistable {
    protected boolean x;

    public void setX(boolean x) {
	this.x = x;
    }

    public boolean getX() {
	return x;
    }
}
