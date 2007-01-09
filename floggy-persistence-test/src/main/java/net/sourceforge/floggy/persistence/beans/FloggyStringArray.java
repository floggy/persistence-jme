package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class FloggyStringArray implements Persistable {
    protected String[] x;

    public void setX(String[] x) {
	this.x = x;
    }

    public String[] getX() {
	return x;
    }
}
