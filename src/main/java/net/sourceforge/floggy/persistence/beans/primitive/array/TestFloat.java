package net.sourceforge.floggy.persistence.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Le�o Moreira <thiagolm@users.sourceforge.net>
 */
public class TestFloat implements Persistable {
    protected float[] x;

    public void setX(float[] x) {
	this.x = x;
    }

    public float[] getX() {
	return x;
    }
}
