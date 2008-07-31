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
package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStringBuffer;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StringBufferTest extends AbstractTest {

	public final static StringBuffer stringBuffer= new StringBuffer("floggy test");
	
	protected Class getParameterType() {
		return StringBuffer.class;
	}
	
	public Object getNewValueForSetMethod() {
		return new StringBuffer();
	}

	public Object getValueForSetMethod() {
		return stringBuffer;
	}

	public Persistable newInstance() {
		return new FloggyStringBuffer();
	}
	
	protected void equals(Object o1, Object o2) {
		super.equals(o1.toString(), o2.toString());
	}
	
	public Filter getFilter() {
		return new Filter() {
			public boolean matches(Persistable arg0) {
				String temp= ((FloggyStringBuffer)arg0).getX().toString();
				return stringBuffer.toString().equals(temp);
			}
		};
	}

}
