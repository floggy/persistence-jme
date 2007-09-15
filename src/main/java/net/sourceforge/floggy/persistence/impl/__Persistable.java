/**
 *  Copyright 2006 Floggy Open Source Group
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
package net.sourceforge.floggy.persistence.impl;

import javax.microedition.rms.RecordStore;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * An internal <code>interface</code> that holds all methods used by the
 * persistence module. All classes identified as <b>persistable</b> ({@link Persistable})
 * will be modified at compilation time and they will automatically implement all methods of this <code>interface</code>.
 * 
 * @since 1.0
 * @see Persistable
 */
public interface __Persistable extends Persistable {

	public int __getId();

	public void __load(int id) throws Exception;

	public void __deserialize(byte[] buffer) throws Exception;

	public int __save() throws Exception;

	public int __save(RecordStore rs) throws Exception;

	public byte[] __serialize(RecordStore rs) throws Exception;

	public void __delete() throws Exception;

	public PersistableMetadata __getPersistableMetadata();

}