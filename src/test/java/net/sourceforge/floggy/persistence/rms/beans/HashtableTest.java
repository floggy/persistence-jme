/**
 *  Copyright 2006 Floggy Open Source Group
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
package net.sourceforge.floggy.persistence.rms.beans;

import java.util.Calendar;
import java.util.Hashtable;
import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyHashtable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class HashtableTest extends AbstractTest {

	public final static Hashtable hashtable = new Hashtable();
	
	static {
		hashtable.put("test", "test");
		hashtable.put(new Integer(9), "foo");
		hashtable.put("key", new Long(89));
		hashtable.put(Calendar.getInstance(), Boolean.TRUE);
		hashtable.put(new Vector(), Boolean.FALSE);
	}


	public Object getValueForSetMethod() {
		return hashtable;
	}

	public Persistable newInstance() {
		return new FloggyHashtable();
	}

}
