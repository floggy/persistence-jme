package net.sourceforge.floggy.persistence.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestInteger implements Persistable {
    private Integer x;

    public Integer getX() {
	return x;
    }

    public void setX(Integer x) {
	this.x = x;
    }
}
