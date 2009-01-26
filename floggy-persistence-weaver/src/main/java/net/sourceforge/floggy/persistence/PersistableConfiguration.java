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
package net.sourceforge.floggy.persistence;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PersistableConfiguration {
	private static final Log LOG = LogFactory.getLog(PersistableConfiguration.class);
	
	private String className;

	private String recordStoreName;

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getRecordStoreName() {
		return recordStoreName;
	}

	public void setRecordStoreName(String recordStoreName) {
		if (recordStoreName.length() >= 32) {
			LOG.warn("The recordStore name "+recordStoreName+" is bigger than 32 characters. It will be truncated to "+recordStoreName.substring(0, 32));
			recordStoreName = recordStoreName.substring(0, 32);
		}
		this.recordStoreName = recordStoreName;
	}
	
}
