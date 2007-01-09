package net.sourceforge.floggy.persistence.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestBoolean implements Persistable {
    protected Boolean x;

    public void setX(Boolean x) {
	this.x = x;
    }

    public Boolean getX() {
	return x;
    }
}
