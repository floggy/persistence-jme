/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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

package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public abstract class PrimitiveAbstractTest extends AbstractTest {

	public abstract Object getDefaultValue();

	public abstract Object getNewValueForSetMethod();
	
	public void testNullAttribute() throws Exception {
		Persistable object = newInstance();
		int id = manager.save(object);
		assertTrue("Deveria ser diferente de -1!", id != -1);
		object = newInstance();
		manager.load(object, id);
		assertEquals("Deveria ser igual (valor default)!", getDefaultValue(),
				getX(object));
		manager.delete(object);
	}
}
