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
package net.sourceforge.floggy.persistence.impl;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Nameable;
import net.sourceforge.floggy.persistence.Persistable;

/**
 * An internal <code>interface</code> that holds all methods used by the
 * persistence module. All classes identified as <b>persistable</b> ({@link Persistable})
 * will be modified at compilation time and they will automatically implement
 * all methods of this <code>interface</code>.
 * 
 * @since 1.0
 * @see Persistable
 */
public interface __Persistable extends Persistable, Nameable {
	/**
	 * DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public void __delete() throws FloggyException;

	/**
	 * DOCUMENT ME!
	*
	* @param buffer DOCUMENT ME!
	* @param lazy DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void __deserialize(byte[] buffer, boolean lazy)
		throws Exception;

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int __getId();

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public byte[] __serialize() throws Exception;

	/**
	 * DOCUMENT ME!
	*
	* @param id DOCUMENT ME!
	*/
	public void __setId(int id);
}
