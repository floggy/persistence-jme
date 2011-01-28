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

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyCalendar;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class CalendarTest extends AbstractTest {
	public static final Calendar calendar = Calendar.getInstance();

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return calendar;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new FloggyCalendar();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param params DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class[] getClassesFromObjects(Object[] params) {
		return new Class[] { Calendar.class };
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class getParameterType() {
		return Calendar.class;
	}
}
