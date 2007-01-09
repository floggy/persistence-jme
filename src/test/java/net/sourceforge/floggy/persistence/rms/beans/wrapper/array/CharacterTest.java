package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestCharacter;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class CharacterTest extends AbstractTest {

    static Character[] att = new Character[] { new Character('u'),
	    new Character(','), new Character((char) 2), new Character('5') };

    public Persistable newInstance() {
	return new TestCharacter();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
