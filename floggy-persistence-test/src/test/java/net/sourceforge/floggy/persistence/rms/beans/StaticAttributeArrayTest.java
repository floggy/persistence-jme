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
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStaticAttributeArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StaticAttributeArrayTest extends AbstractTest {

	protected Class getParameterType() {
		return Object[].class;
	}
	
	public Object getNewValueForSetMethod() {
		return new Object[0];
	}

	public Object getValueForSetMethod() {
		return new Object[] { "floggy-framework", TransientTest.object };
	}

	public Persistable newInstance() {
		return new FloggyStaticAttributeArray();
	}

	protected void tearDown() throws Exception {
		FloggyStaticAttributeArray.x = null;
	}
	
	public Filter getFilter() {
		return new Filter() {
			public boolean matches(Persistable o) {
				return false;
			}
		};
	}
	
	public void testFindWithFilter() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);
		ObjectSet set = manager.find(object.getClass(), getFilter(), null);
		assertEquals(0, set.size());
		manager.delete(object);
	}

	public void testNotNullAttribute() throws Exception {
		// como o atributo não vai ser salvo ele tem q retornar null!!!
		super.testNullAttribute();
	}

}
