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

package net.sourceforge.floggy.persistence.rms.beans;

import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyVectorArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class VectorArrayTest extends AbstractTest {

	protected Class getParameterType() {
		return Vector[].class;
	}
	public Object getNewValueForSetMethod() {
		return new Vector[0];
	}

	public Object getValueForSetMethod() {
		return new Vector[] { new Vector(), null, (Vector)new VectorTest().getValueForSetMethod()};
	}

	public Persistable newInstance() {
		return new FloggyVectorArray();
	}

}
