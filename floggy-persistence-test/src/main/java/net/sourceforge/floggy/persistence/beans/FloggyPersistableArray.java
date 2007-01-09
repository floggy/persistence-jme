package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class FloggyPersistableArray implements Persistable {
    protected FloggyPersistable[] x;

    public void setX(FloggyPersistable[] x) {
	this.x = x;
    }

    public FloggyPersistable[] getX() {
	return x;
    }
}
