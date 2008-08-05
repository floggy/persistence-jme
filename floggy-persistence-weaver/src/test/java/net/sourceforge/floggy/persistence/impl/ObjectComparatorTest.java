package net.sourceforge.floggy.persistence.impl;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.Comparator;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class ObjectComparatorTest extends TestCase {
	
	protected Mockery context= new Mockery();
	
	public void testCompare() throws Exception {
	     // set up
		final byte[] data= new byte[]{};
		
        final __Persistable p1 = (__Persistable)context.mock(__Persistable.class);
        final __Persistable p2 = (__Persistable)context.mock(__Persistable.class);
        
        final Comparator comparator= (Comparator) context.mock(Comparator.class);

        ObjectComparator objectComparator= new ObjectComparator(comparator, p1, p2);

        // expectations
        context.checking(new Expectations() {{
            ((__Persistable)one(p1)).__deserialize(data);
            ((__Persistable)one(p2)).__deserialize(data);
            ((Comparator)one(comparator)).compare(p1, p2);
            will(returnValue(new Integer(0)));
        }});

        // execute
        assertEquals(0, objectComparator.compare(data, data));
        
        // verify
        context.assertIsSatisfied();
		
	}

}
