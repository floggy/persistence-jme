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

import java.util.TimeZone;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyTimeZone;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class TimeZoneTest extends AbstractTest {

	public final static TimeZone timeZone = TimeZone.getDefault();

	protected Class getParameterType() {
		return TimeZone.class;
	}
	public Object getNewValueForSetMethod() {
		return TimeZone.getTimeZone("PST");
	}

	public Object getValueForSetMethod() {
		return timeZone;
	}

	public Persistable newInstance() {
		return new FloggyTimeZone();
	}
	
	protected Class[] getClassesFromObjects(Object[] params) {
		return new Class[]{TimeZone.class};
	}

}