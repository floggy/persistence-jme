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

import java.util.Stack;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStackArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StackArrayTest extends AbstractTest {

	protected Class getParameterType() {
		return Stack[].class;
	}
	
	public Object getNewValueForSetMethod() {
		return new Stack[0];
	}

	public Object getValueForSetMethod() {
		return new Stack[] { StackTest.stack, null, new Stack() };
	}

	public Persistable newInstance() {
		return new FloggyStackArray();
	}

}
