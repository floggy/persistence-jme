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
