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
package net.sourceforge.floggy.persistence.impl.migration;

import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;
import net.sourceforge.floggy.persistence.impl.Utils;
import net.sourceforge.floggy.persistence.impl.__Persistable;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

public class MigrationManagerImpl extends MigrationManager {
	
	protected Hashtable enumerations = new Hashtable();
	
	public MigrationManagerImpl() throws Exception {
		PersistableManager.getInstance();
	}

	public void finish(Class persistableClass) throws FloggyException {

		Utils.validatePersistableClassArgument(persistableClass);

		AbstractEnumerationImpl impl = (AbstractEnumerationImpl) enumerations.get(persistableClass);
		if (impl != null) {
			impl.finish();
		}
	}
	
	public String[] getNotMigratedClasses() {
		Vector notMigratedClasses = PersistableMetadataManager.getNotMigratedClasses();
		String[] temp = new String[notMigratedClasses.size()];
		notMigratedClasses.copyInto(temp);
		return temp;
	}
	
	public void quickMigration(Class persistableClass) throws FloggyException {

		Utils.validatePersistableClassArgument(persistableClass);
		
		String className = persistableClass.getName();
		PersistableMetadata classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(className);
		PersistableMetadata rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(className);
		
		if (rmsBasedMetadata != null) {
			if (!classBasedMetadata.equals(rmsBasedMetadata)) {
				throw new FloggyException("Class and RMS description doesn't match for class " + className + ". Please execute a normal migration process.");
			}
		} else {
			try {
				PersistableMetadataManager.saveRMSStructure(classBasedMetadata);
			} catch (Exception ex) {
				throw Utils.handleException(ex);
			}
		}
	}
	
	public Enumeration start(Class persistableClass, Hashtable properties)
			throws FloggyException {
		
		Utils.validatePersistableClassArgument(persistableClass);

		PersistableMetadata classBasedMetadata = PersistableMetadataManager.getClassBasedMetadata(persistableClass.getName()); 

		if (classBasedMetadata.isAbstract()) {
			throw new FloggyException("It is not possible migrate abstract classes. Instead migrate its subclasses");
		}
		
		boolean lazyLoad = false;
		boolean migrateFromPreviousFloggyVersion = false;
		boolean iterationMode = false;
		if (properties != null) {
			Object temp = properties.get(MigrationManager.LAZY_LOAD);
			if (temp instanceof Boolean) {
				lazyLoad = ((Boolean)temp).booleanValue();
			}

			temp = properties.get(MigrationManager.MIGRATE_FROM_PREVIOUS_1_3_0_VERSION);
			if (temp instanceof Boolean) {
				migrateFromPreviousFloggyVersion = ((Boolean)temp).booleanValue();
			}

			temp = properties.get(MigrationManager.ITERATION_MODE);
			if (temp instanceof Boolean) {
				iterationMode = ((Boolean)temp).booleanValue();
			}
		}
		
		RecordStore rs = null;
		Enumeration impl = null;
		PersistableMetadata rmsBasedMetadata = null; 

		try {
			rmsBasedMetadata = PersistableMetadataManager.getRMSBasedMetadata(persistableClass.getName());

			if (rmsBasedMetadata == null) {
				if (migrateFromPreviousFloggyVersion) {
					rmsBasedMetadata = classBasedMetadata;
				} else {
					throw new FloggyException("Set the property MigrationManager.MIGRATE_FROM_PREVIOUS_1_3_0_VERSION to true to migrate from a version lower than 1.3.0.");
				}
			}
			
			if (classBasedMetadata.getPersistableStrategy() == PersistableMetadata.SINGLE_STRATEGY &&
					rmsBasedMetadata.getPersistableStrategy() == PersistableMetadata.JOINED_STRATEGY) {
				
				rs = RecordStoreManager.getRecordStore(rmsBasedMetadata.getRecordStoreName(), rmsBasedMetadata, true);
			} else {
				__Persistable persistable = Utils.createInstance(persistableClass);

				rs = RecordStoreManager.getRecordStore(persistable.getRecordStoreName(), classBasedMetadata, true);
			}
			
			RecordEnumeration en = rs.enumerateRecords(null, null, false);

			Hashtable persistableImplementations = rmsBasedMetadata.getPersistableImplementations();
			if (!lazyLoad && persistableImplementations != null) {
				java.util.Enumeration classNames = persistableImplementations.elements();
				while (classNames.hasMoreElements()) {
					String className = (String) classNames.nextElement();
					if (className.endsWith("[]")) {
						className = className.substring(0, className.length() - 2);
					}
					PersistableMetadata fieldClassMetadata = PersistableMetadataManager.getClassBasedMetadata(className);
					PersistableMetadata fieldRMSMetadata = PersistableMetadataManager.getRMSBasedMetadata(className);
					if (fieldClassMetadata != null && fieldRMSMetadata != null && !fieldClassMetadata.equals(fieldRMSMetadata)) {
						throw new FloggyException("You first must migrate the class " + className + " than you can migrate " + persistableClass.getName());
					}
				}
			}

			switch (rmsBasedMetadata.getPersistableStrategy()) {
				case PersistableMetadata.SINGLE_STRATEGY:
					impl = new SingleStrategyEnumerationImpl(rmsBasedMetadata, classBasedMetadata, en, rs, lazyLoad, iterationMode);
					break;
				case PersistableMetadata.PER_CLASS_STRATEGY:
					impl = new PerClassStrategyEnumerationImpl(rmsBasedMetadata, classBasedMetadata, en, rs, lazyLoad, iterationMode);
					break;
				case PersistableMetadata.JOINED_STRATEGY:
					impl = new JoinedStrategyEnumerationImpl(rmsBasedMetadata, classBasedMetadata, en, rs, lazyLoad, iterationMode);
					break;
				default:
					break;
			}
			enumerations.put(persistableClass, impl);
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}

		return impl;
	}

}
