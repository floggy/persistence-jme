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
package net.sourceforge.floggy.persistence.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * @author Thiago Le�o Moreira <thiagolm@users.sourceforge.net>
 */
public class TestBoolean implements Persistable {
	protected boolean x;

	public boolean getX() {
		return x;
	}

	public void setX(boolean x) {
		this.x = x;
	}
}
