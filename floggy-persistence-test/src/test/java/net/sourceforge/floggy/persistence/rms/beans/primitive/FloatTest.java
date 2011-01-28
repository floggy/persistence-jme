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
package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestFloat;

public class FloatTest extends PrimitiveAbstractTest {

	protected Class getParameterType() {
		return float.class;
	}
	
	public Object getDefaultValue() {
		return new Float(0.0);
	}
	
	public Object getNewValueForSetMethod() {
		return new Float((float)654);
	}

	public Object getValueForSetMethod() {
		return new Float((float) 23.0987);
	}

	public Persistable newInstance() {
		return new TestFloat();
	}

}
