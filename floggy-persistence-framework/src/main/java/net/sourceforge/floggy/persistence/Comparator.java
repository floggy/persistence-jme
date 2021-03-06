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
package net.sourceforge.floggy.persistence;

/**
 * An interface defining a comparator which compares two objects (in an
 * implementation-defined manner) to see if they match or what their relative
 * sort order is. The application implements this interface to compare two
 * candidate objects. The return value must indicate the ordering of the two
 * records. <br>
 * The compare method is called by <code>PersistableManager</code> to sort the
 * results of a search.<br>
 * 
 * @since 1.0
 * 
 * @see PersistableManager#find(Class, Filter, Comparator)
 * @see Filter
 */
public interface Comparator {
	/**
	* EQUIVALENT means that in terms of search or sort order, the two
	* objects are the same. This does not necessarily mean that the two objects
	* are identical.<br>
	* <br>
	* The value of EQUIVALENT is 0.
	 */
	int EQUIVALENT = 0;

	/**
	* FOLLOWS means that the left (first parameter) object follows the
	* right (second parameter) object in terms of search or sort order.<br>
	* <br>
	* The value of FOLLOWS is 1.
	 */
	int FOLLOWS = 1;

	/**
	* PRECEDES means that the left (first parameter) object precedes the
	* right (second parameter) object in terms of search or sort order.<br>
	* <br>
	* The value of PRECEDES is -1.
	 */
	int PRECEDES = -1;

	/**
	* Returns <code>Comparator.PRECEDES</code> if <code>o1</code> precedes
	* <code>o2</code> in sort order, or <code>Comparator.FOLLOWS</code> if
	* <code>o1</code> follows <code>o2</code> in sort order, or
	* <code>Comparator.EQUIVALENT</code> if <code>o1</code> and <code>o2</code>
	* are equivalent in terms of sort order.
	*
	* @param o1 The first object for comparison.
	* @param o2 The second object for comparison.
	*
	* @return <code>Comparator.PRECEDES</code> if <code>o1</code> precedes
	* 				<code>o2</code> in sort order, or <code>Comparator.FOLLOWS</code>
	* 				if <code>o1</code> follows <code>o2</code> in sort order, or
	* 				<code>Comparator.EQUIVALENT</code> if <code>o1</code> and
	* 				<code>o2</code> are equivalent in terms of sort order.
	*/
	int compare(Persistable o1, Persistable o2);
}
