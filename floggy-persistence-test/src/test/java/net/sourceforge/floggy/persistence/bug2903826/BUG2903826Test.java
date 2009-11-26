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

package net.sourceforge.floggy.persistence.bug2903826;

import java.util.Stack;
import java.util.Vector;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.beans.FloggyStack;
import net.sourceforge.floggy.persistence.beans.FloggyVector;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;

public class BUG2903826Test extends FloggyBaseTest {
	
	public void testLazyInVectors() throws Exception {
		Person person = new Person();
		person.setNome("Thiago Moreira");
		
		Bird bird = new Bird();
		bird.setColor("blue");
		
		Vector vector = new Vector();
		vector.addElement(person);
		vector.addElement(bird);
		
		FloggyVector floggy = new FloggyVector();
		floggy.setX(vector);
		
		try {
			int id = manager.save(floggy);

			floggy = new FloggyVector();
			
			manager.load(floggy, id, true);
			
			vector = floggy.getX();

			assertEquals(2, vector.size());
			assertNull(vector.get(0));
			assertNull(vector.get(1));
		} catch (Exception e){
			e.printStackTrace();
			fail(e.getMessage());
		} finally {
			manager.delete(bird);
			manager.delete(person);
			manager.delete(floggy);
		}
	}

	public void testLazyInStacks() throws Exception {
		Person person = new Person();
		person.setNome("Thiago Moreira");
		
		Long number = new Long(6549876);
		
		Stack stack = new Stack();
		stack.push(person);
		stack.push(number);
		
		FloggyStack floggy = new FloggyStack();
		floggy.setX(stack);
		
		try {
			int id = manager.save(floggy);

			floggy = new FloggyStack();
			
			manager.load(floggy, id, true);
			
			stack = floggy.getX();
			
			assertEquals(2, stack.size());
			assertNotNull(stack.pop());
			assertNull(stack.pop());
		} finally {
			manager.delete(person);
			manager.delete(floggy);
		}
	}

}
