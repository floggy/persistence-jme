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
package net.sourceforge.floggy.persistence.bug2313565;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyPersistable;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.EastUSFalcon;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class BUG2313565Test extends AbstractTest {

	public final static Bird persistable = new Bird();

	protected Class getParameterType() {
		return Bird.class;
	}

	public Object getNewValueForSetMethod() {
		return new Falcon();
	}

	public Object getValueForSetMethod() {
		return persistable;
	}

	public Persistable newInstance() {
		return new BUG2313565();
	}

	public void testLoadLazyTrue() throws Exception {
		BUG2313565 container = new BUG2313565();
		container.w = true;
		container.y = 22;
		Bird field = new EastUSFalcon();

		int containerId;

		container.setX(field);

		try {
			containerId = manager.save(container);

			BUG2313565 container2 = new BUG2313565();
			manager.load(container2, containerId, true);

			assertNull(container2.getX());
			assertEquals(container.w, container2.w);
			assertEquals(container.y, container2.y);
		} finally {
			manager.deleteAll(FloggyPersistable.class);
		}

	}

	public void testLoadLazyFalse() throws Exception {
		BUG2313565 container = new BUG2313565();
		container.w = true;
		container.y = 22;
		Bird field = new EastUSFalcon();

		int containerId;

		container.setX(field);

		try {
			containerId = manager.save(container);

			BUG2313565 container2 = new BUG2313565();
			manager.load(container2, containerId, false);

			assertEquals(container.w, container2.w);
			assertEquals(container.getX(), container2.getX());
			assertEquals(container.y, container2.y);
		} finally {
			manager.deleteAll(FloggyPersistable.class);
		}

	}

}
