package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestDouble implements Persistable {
    protected double x;

    public void setX(double x) {
	this.x = x;
    }

    public double getX() {
	return x;
    }
}
