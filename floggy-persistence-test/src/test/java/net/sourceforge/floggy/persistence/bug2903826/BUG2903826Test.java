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

package net.sourceforge.floggy.persistence.bug2903826;

import java.util.Stack;
import java.util.Vector;

import net.sourceforge.floggy.persistence.FloggyBaseTest;

public class BUG2903826Test extends FloggyBaseTest {
	
	public void testLazyInVectors() throws Exception {
		BUG2903826 bug2903826 = new BUG2903826();
		bug2903826.setName("Thiago Moreira");
		
		Vector vector = new Vector();
		vector.addElement(bug2903826);
		vector.addElement(null);
		
		BUG2903826VectorHolder holder = new BUG2903826VectorHolder();
		holder.setVector(vector);
		
		try {
			int id = manager.save(holder);

			holder = new BUG2903826VectorHolder();
			
			manager.load(holder, id, true);
			
			vector = holder.getVector();

			assertEquals(2, vector.size());
			assertNull(vector.get(0));
			assertNull(vector.get(1));
		} finally {
			manager.delete(bug2903826);
			manager.delete(holder);
		}
	}

	public void testLazyInStacks() throws Exception {
		BUG2903826 bug2903826 = new BUG2903826();
		bug2903826.setName("Thiago Moreira");
		
		Stack stack = new Stack();
		stack.push(bug2903826);
		stack.push(new Integer(321));
		
		BUG2903826StackHolder holder = new BUG2903826StackHolder();
		holder.setStack(stack);
		
		try {
			int id = manager.save(holder);

			holder = new BUG2903826StackHolder();
			
			manager.load(holder, id, true);
			
			stack = holder.getStack();
			
			assertEquals(2, stack.size());
			assertNotNull(stack.pop());
			assertNull(stack.pop());
		} finally {
			manager.delete(bug2903826);
			manager.delete(holder);
		}
	}

}
