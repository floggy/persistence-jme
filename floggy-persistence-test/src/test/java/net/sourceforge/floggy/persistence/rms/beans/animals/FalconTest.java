package net.sourceforge.floggy.persistence.rms.beans.animals;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class FalconTest extends AbstractTest {

	protected Class getParameterType() {
		return Bird.class;
	}

	public Object getValueForSetMethod() {
		Bird bird= new Falcon();
		bird.setColor("black");
		return bird;
	}

	public Persistable newInstance() {
		return new Person();
	}

}
