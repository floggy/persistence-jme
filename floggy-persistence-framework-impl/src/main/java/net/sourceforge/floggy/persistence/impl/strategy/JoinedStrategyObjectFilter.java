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
package net.sourceforge.floggy.persistence.impl.strategy;

import javax.microedition.rms.RecordFilter;

import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.impl.__Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class JoinedStrategyObjectFilter implements RecordFilter {
	private final Filter f;
	private final __Persistable p;
	private final boolean lazy;

	/**
	 * Creates a new JoinedStrategyObjectFilter object.
	 *
	 * @param lazy DOCUMENT ME!
	 */
	public JoinedStrategyObjectFilter(boolean lazy) {
		this.p = null;
		this.f = null;
		this.lazy = lazy;
	}

	/**
	 * Creates a new JoinedStrategyObjectFilter object.
	 *
	 * @param p DOCUMENT ME!
	 * @param f DOCUMENT ME!
	 * @param lazy DOCUMENT ME!
	 */
	public JoinedStrategyObjectFilter(__Persistable p, Filter f, boolean lazy) {
		this.p = p;
		this.f = f;
		this.lazy = lazy;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param b DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean matches(byte[] b) {
		if (b[3] == 0) {
			return false;
		}

		if (f != null) {
			try {
				p.__deserialize(b, lazy);
			} catch (Exception e) {
			}

			return f.matches(p);
		}

		return true;
	}
}
