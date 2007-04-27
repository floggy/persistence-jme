package net.sourceforge.floggy.persistence;

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
