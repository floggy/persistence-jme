package net.sourceforge.floggy.persistence;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Configuration {
	
	private boolean addDefaultConstructor = true;

	private boolean generateSource = false;
	
	private List persistables= new ArrayList();

	public List getPersistables() {
		return persistables;
	}

	public boolean isAddDefaultConstructor() {
		return addDefaultConstructor;
	}

	public void setAddDefaultConstructor(boolean addDefaultConstructor) {
		this.addDefaultConstructor = addDefaultConstructor;
	}

	public boolean isGenerateSource() {
		return generateSource;
	}

	public void setGenerateSource(boolean generateSource) {
		this.generateSource = generateSource;
	}

	public void setPersistables(List persistables) {
		this.persistables = persistables;
	}
	
	public PersistableConfiguration getPersistableConfig(String className) {
		for (Iterator iter = persistables.iterator(); iter.hasNext();) {
			PersistableConfiguration config = (PersistableConfiguration) iter.next();
			if (className.equals(config.getClassName())) {
				return config;
			}
		}
		return null;
	}
	
	public boolean containsPersistable(String className) {
		return getPersistableConfig(className) != null;
	}
	
	public void addPersistable(PersistableConfiguration config) {
		persistables.add(config);
	}

}
