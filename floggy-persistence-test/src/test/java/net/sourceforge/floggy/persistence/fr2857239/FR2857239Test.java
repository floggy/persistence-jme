package net.sourceforge.floggy.persistence.fr2857239;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;

public class FR2857239 extends TestCase {
	
	public void testIt() throws Exception {
		PersistableManager manager = PersistableManager.getInstance();

		Bird bird = new Falcon();
		Person person = new Person();
		person.setX(bird);
		
		try {

			int id = manager.save(person);
			assertTrue("Deveria ser diferente de -1!", id != -1);
			
			ObjectSet os = manager.find(person.getClass(), null, null);
			assertFalse(os.isLazy());
			os.setLazy(true);
			assertTrue(os.isLazy());
			for (int i = 0; i < os.size(); i++) {
				assertNull(((Person)os.get(i)).getX());
			}

			os = manager.find(person.getClass(), null, null);
			assertFalse(os.isLazy());
			for (int i = 0; i < os.size(); i++) {
				assertNotNull(((Person)os.get(i)).getX());
			}
		} finally {
			manager.delete(person);
		}
	}

}
