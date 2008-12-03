package net.sourceforge.floggy.persistence.bug2168632;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.PersistableManager;

public class BUG2168632Test extends TestCase {

    public void testIt() throws Exception {
	ConcreteElement persistable = new ConcreteElement();
	persistable.setName("floggy");
	
	try {
	    int id = PersistableManager.getInstance().save(persistable);
	    
	    persistable = new ConcreteElement();
	    
	    PersistableManager.getInstance().load(persistable, id);
	    assertTrue(true);
	} catch (Exception e) {
	    fail(e.getMessage());
	} finally {
	    PersistableManager.getInstance().deleteAll(ConcreteElement.class);
	}
    }
}
