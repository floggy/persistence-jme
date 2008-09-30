package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class InheranceDeleteTest extends AbstractTest {

	static Boolean x = Boolean.TRUE;

	protected Class getParameterType() {
		return Boolean.class;
	}

	public Object getValueForSetMethod() {
		return x;
	}

	public Persistable newInstance() {
		return new Falcon();
	}

	/**
	 * Testcase to reproduce this bug
	 * http://sourceforge.net/tracker/index.php?func=detail&aid=2105288&group_id=139426&atid=743541
	 */
	public void testInheranceDelete() throws Exception {
		Falcon falcon = new Falcon();
		int id = manager.save(falcon);

		//
		falcon = new Falcon();
		manager.load(falcon, id);
		
		manager.delete(falcon);

	}

}
