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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import net.sourceforge.floggy.persistence.impl.IndexMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.Utils;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Configuration {
	private List persistables = new ArrayList();
	private boolean addDefaultConstructor = true;
	private boolean generateSource = false;

	/**
	 * DOCUMENT ME!
	*
	* @param metadata DOCUMENT ME!
	*/
	public void addPersistableMetadata(PersistableMetadata metadata) {
		persistables.add(metadata);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean containsPersistable(String className) {
		return getPersistableMetadata(className) != null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public PersistableMetadata getPersistableMetadata(String className) {
		for (Iterator iter = persistables.iterator(); iter.hasNext();) {
			PersistableMetadata metadata = (PersistableMetadata) iter.next();

			if (className.equals(metadata.getClassName())) {
				return metadata;
			}
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public List getPersistableMetadatas() {
		return persistables;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean isAddDefaultConstructor() {
		return addDefaultConstructor;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean isGenerateSource() {
		return generateSource;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param addDefaultConstructor DOCUMENT ME!
	*/
	public void setAddDefaultConstructor(boolean addDefaultConstructor) {
		this.addDefaultConstructor = addDefaultConstructor;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param generateSource DOCUMENT ME!
	*/
	public void setGenerateSource(boolean generateSource) {
		this.generateSource = generateSource;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistables DOCUMENT ME!
	*/
	public void setPersistables(List persistables) {
		this.persistables = persistables;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String toString() {
		return "Configuration [addDefaultConstructor=" + addDefaultConstructor
		+ ", generateSource=" + generateSource + ", persistables=" + persistables
		+ "]";
	}
}
