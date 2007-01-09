package net.sourceforge.floggy.persistence.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class TestCharacter implements Persistable {
    protected Character x;

    public void setX(Character x) {
	this.x = x;
    }

    public Character getX() {
	return x;
    }
}
