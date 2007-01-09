package net.sourceforge.floggy.persistence.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestFloat implements Persistable {
    protected Float x;

    public void setX(Float x) {
	this.x = x;
    }

    public Float getX() {
	return x;
    }
}
