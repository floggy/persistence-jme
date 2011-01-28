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

import java.util.Hashtable;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStaticAttribute;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StaticAttributeTest extends AbstractTest {

	static final Object object = new Object();

	protected Class getParameterType() {
		return Object.class;
	}

	public Object getNewValueForSetMethod() {
		return new Object();
	}

	public Object getValueForSetMethod() {
		return object;
	}

	public Persistable newInstance() {
		return new FloggyStaticAttribute();
	}

	protected void tearDown() throws Exception {
		FloggyStaticAttribute.x = null;
	}

	public void testFR2422928Read() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);
		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(object.getClass(), null);
		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertTrue(data.isEmpty());
			}
		} finally {
			manager.delete(object);
			um.finish(object.getClass());
		}
	}

	public void testNotNullAttribute() throws Exception {
		// como o atributo não vai ser salvo ele tem q retornar null!!!
		testNullAttribute();
	}

}
