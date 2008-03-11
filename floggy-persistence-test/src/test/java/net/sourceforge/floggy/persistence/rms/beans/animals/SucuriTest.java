package net.sourceforge.floggy.persistence.rms.beans.animals;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.animals.Reptile;
import net.sourceforge.floggy.persistence.beans.animals.Sucuri;

public class SucuriTest extends TestCase {
	
	public void testDeleteWithSuperClassBeenPersistable() throws Exception {
		PersistableManager manager= PersistableManager.getInstance();
		Sucuri sucuri= new Sucuri();
		manager.save(sucuri);
		assertTrue(manager.isPersisted(sucuri));
		ObjectSet set= manager.find(Reptile.class, null, null);
		assertEquals(1, set.size());
		
		manager.delete(sucuri);
		assertFalse(manager.isPersisted(sucuri));
		set= manager.find(Reptile.class, null, null);
		assertEquals(0, set.size());
	}

}
