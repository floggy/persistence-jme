/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.persistence.rms.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TimeZone;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyHashtable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class HashtableTest extends AbstractTest {

	public final static Hashtable hashtable = new Hashtable();

	static {
		hashtable.put(Boolean.TRUE, Boolean.TRUE);
		hashtable.put(new Byte((byte) 90), new Byte((byte) 90));
		hashtable.put(new Character('2'), new Character('2'));
		hashtable.put(new Double(23d), new Double(23d));
		hashtable.put(new Float(45f), new Float(45f));
		hashtable.put(new Integer(87), new Integer(87));
		hashtable.put(new Long(89), new Long(89));
		hashtable.put(new Short((short)78), new Short((short)78));
		hashtable.put("key", "value");
		hashtable.put(new Date(), new Date());
		hashtable.put(Calendar.getInstance(), Calendar.getInstance());
		hashtable.put(TimeZone.getDefault(), TimeZone.getDefault());
	}

	public Object getValueForSetMethod() {
		return hashtable;
	}

	public Persistable newInstance() {
		return new FloggyHashtable();
	}

	protected Class getParameterType() {
		return Hashtable.class;
	}

}
