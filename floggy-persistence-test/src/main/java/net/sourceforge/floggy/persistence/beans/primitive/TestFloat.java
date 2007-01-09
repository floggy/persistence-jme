package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestFloat implements Persistable {
    protected float x;

    public void setX(float x) {
	this.x = x;
    }

    public float getX() {
	return x;
    }
}
