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

package net.sourceforge.floggy.persistence.fr2422928;

import java.util.Calendar;

import net.sourceforge.floggy.persistence.Nameable;
import net.sourceforge.floggy.persistence.Persistable;

public class FR2422928 implements Persistable, Nameable {

	protected Calendar checkpoint;
//	protected long id;
	protected String name;
	protected Persistable node;

	public Calendar getCheckpoint() {
		return checkpoint;
	}

//	public long getId() {
//		return id;
//	}

	public String getName() {
		return name;
	}

	public Persistable getNode() {
		return node;
	}

	public String getRecordStoreName() {
		return "FR2422928";
	}

	public void setCheckpoint(Calendar checkpoint) {
		this.checkpoint = checkpoint;
	}

	public void setName(String name) {
		this.name = name;
	}

//	public void setId(long id) {
//		this.id = id;
//	}

	public void setNode(Persistable node) {
		this.node = node;
	}
}
