/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
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

        ObjectFilter objectFilter= new ObjectFilter(persistable, filter, false);

        // expectations
        context.checking(new Expectations() {{
            ((__Persistable)one(persistable)).__deserialize(data, false);
            ((Filter)one(filter)).matches(persistable);
            will(returnValue(Boolean.TRUE));
        }});

        // execute
        assertTrue(objectFilter.matches(data));
        
        // verify
        context.assertIsSatisfied();
		
	}

}
