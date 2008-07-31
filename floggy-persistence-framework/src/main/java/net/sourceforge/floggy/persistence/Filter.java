/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.persistence;

/**
 * An interface defining a filter which examines a object to see if it matches
 * (based on an application-defined criteria). The application implements the
 * <code>match(Persistable object)</code> method to select objects. Returns
 * true if the candidate record is selected by the RecordFilter. This interface
 * is used in the record store for searching or subsetting records.
 * 
 * @since 1.0
 * 
 * @see PersistableManager#find(Class, Filter, Comparator)
 * @see Comparator
 */
public interface Filter {

	/**
	 * Returns true if the candidate object matches the implemented criterion.
	 * 
	 * @param o
	 *            The candidate object.
	 * @return True if the candidate object matches the implemented criterion.
	 */
	public boolean matches(Persistable o);

}
