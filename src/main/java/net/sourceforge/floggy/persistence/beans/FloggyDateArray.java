package net.sourceforge.floggy.persistence.beans;

import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class FloggyDateArray implements Persistable {
    protected Date x[];

    public void setX(Date[] x) {
	this.x = x;
    }

    public Date[] getX() {
	return x;
    }
}
