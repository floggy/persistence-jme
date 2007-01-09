package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestCharacter;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class CharacterTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestCharacter();
    }

    public Object getValueForSetMethod() {
	return new Character('>');
    }

}
