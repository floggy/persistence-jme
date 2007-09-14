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

import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyTransient;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class TransientTest extends AbstractTest {

	static final Object object = new Object();

	public Object getValueForSetMethod() {
		return object;
	}

	public Persistable newInstance() {
		return new FloggyTransient();
	}

	public void testFind() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);
		ObjectSet set = manager.find(object.getClass(), getFilter(), null);
		assertEquals(0, set.size());
		manager.delete(object);
	}

	public void testNotNullAttribute() throws Exception {
		// como o atributo n�o vai ser salvo ele tem q retornar null!!!
		super.testNullAttribute();
	}

}
