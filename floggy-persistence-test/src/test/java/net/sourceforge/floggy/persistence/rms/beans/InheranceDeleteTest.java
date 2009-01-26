/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class InheranceDeleteTest extends AbstractTest {

	static Boolean x = Boolean.TRUE;

	protected Class getParameterType() {
		return Boolean.class;
	}

	public Object getValueForSetMethod() {
		return x;
	}

	public Persistable newInstance() {
		return new Falcon();
	}

	/**
	 * Testcase to reproduce this bug
	 * http://sourceforge.net/tracker/index.php?func=detail&aid=2105288&group_id=139426&atid=743541
	 */
	public void testInheranceDelete() throws Exception {
		Falcon falcon = new Falcon();
		int id = manager.save(falcon);

		//
		falcon = new Falcon();
		manager.load(falcon, id);
		
		manager.delete(falcon);

	}

}
