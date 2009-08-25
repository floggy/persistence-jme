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
package net.sourceforge.floggy.persistence.migration;

import java.util.Hashtable;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;

/**
 * A Enumeration to be used in the updating process. A instance of this 
 * interface will iterate over a collection of registers that must be migrated
 * from old layout of fields to a new layout.  
 *
 * @author Thiago Moreira
 * @since 1.3.0
  */
public interface Enumeration {
    /**
     * Tests if this enumeration contains more elements.
     *
     * @return <code>true</code> if and only if this enumeration object
     *         contains at least one more element to provide;
     *         <code>false</code> otherwise.
     */
    boolean hasMoreElements();

    /**
     * Returns the next element of this enumeration if this enumeration
     * object has at least one more element to provide.
     *
     * @return the next element of this enumeration.
     *
     * @exception FloggyException if no more elements exist.
     */
    Hashtable nextElement() throws FloggyException;

    /**
     * Delete the current record.
     *
     * @return the recordId where the persistable object was deleted from.
     *
     * @throws FloggyException
     */
    int delete() throws FloggyException;

    /**
     * Gets the size of the enumeration.
     *
     * @return the size
     */
    int getSize();

    /**
     * Update the current record with the Persistable parameter.
     *
     * @param persistable The persistable object that will update the current
     *        record.
     *
     * @return the recordId where the persistable object is stored
     *
     * @throws FloggyException
     */
    int update(Persistable persistable) throws FloggyException;
}
