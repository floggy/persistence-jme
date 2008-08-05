package net.sourceforge.floggy.persistence.impl;

import net.sourceforge.floggy.persistence.Filter;

import org.jmock.Expectations;
import org.jmock.Mockery;

import junit.framework.TestCase;

public class ObjectFilterTest extends TestCase {
	
	protected Mockery context= new Mockery();
	
	public void testMatches() throws Exception {
	     // set up
		final byte[] data= new byte[]{};
		
        final __Persistable persistable = (__Persistable)context.mock(__Persistable.class);
        
        final Filter filter= (Filter) context.mock(Filter.class);

        ObjectFilter objectFilter= new ObjectFilter(persistable, filter);

        // expectations
        context.checking(new Expectations() {{
            ((__Persistable)one(persistable)).__deserialize(data);
            ((Filter)one(filter)).matches(persistable);
            will(returnValue(Boolean.TRUE));
        }});

        // execute
        assertTrue(objectFilter.matches(data));
        
        // verify
        context.assertIsSatisfied();
		
	}

}
