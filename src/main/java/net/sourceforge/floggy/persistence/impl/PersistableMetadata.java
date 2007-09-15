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

/**
 * <b>IMPORTANT:</b> This class is for internal use only.
 */
public class PersistableMetadata {
	
	private String recordStoreName;
	
	
	public PersistableMetadata(String recordStoreName) {
		super();
		this.recordStoreName = recordStoreName;
	}

	public String getRecordStoreName() {
		return recordStoreName;
	}

	public void setRecordStoreName(String recordStoreName) {
		this.recordStoreName = recordStoreName;
	}

}