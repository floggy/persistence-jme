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
package net.sourceforge.floggy.persistence.migration;

import java.util.Hashtable;

import net.sourceforge.floggy.persistence.FloggyException;

/**
 * DOCUMENT ME!
 * 
 * @author Thiago Moreira
 * 
 * @since 1.3.0
 */
public abstract class MigrationManager {
	
	public static final String LAZY_LOAD = "LAZY_LOAD";
	public static final String MIGRATE_FROM_PREVIOUS_1_3_0_VERSION = "MIGRATE_FROM_PREVIOUS_1_3_0_VERSION";
	public static final String ITERATION_MODE = "ITERATION_MODE";

	/**
	 * The single instance of PersistableManager.
	 */
	private static MigrationManager instance;

	/**
	 * Returns the current instance of PersistableManager.
	 * 
	 * @return The current instance of PersistableManager.
	 * 
	 * @throws RuntimeException
	 *             DOCUMENT ME!
	 */
	public static MigrationManager getInstance() {
		if (instance == null) {
			try {
				Class pmClass = Class
						.forName("net.sourceforge.floggy.persistence.impl.migration.MigrationManagerImpl");
				instance = (MigrationManager) pmClass.newInstance();
			} catch (Exception e) {
				// this would be never happen because the weaver will
				// always embebebed this class
				String message = e.getMessage();

				if (message == null) {
					message = e.getClass().getName();
				}

				throw new RuntimeException(message);
			}
		}

		return instance;
	}

	/**
	 * Finish the whole migration process. At the end it update the layout of
	 * fields of the Persistable class being migrated.
	 * 
	 * @param persistableClass
	 *            DOCUMENT ME!
	 * 
	 * @throws FloggyException
	 *             If the enumeration wasn't whole processed.
	 */
	public abstract void finish(Class persistableClass) throws FloggyException;

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public abstract String[] getNotMigratedClasses();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param persistableClass
	 *            DOCUMENT ME!
	 * 
	 * @throws FloggyException
	 *             DOCUMENT ME!
	 */
	public abstract void quickMigration(Class persistableClass)
			throws FloggyException;

	/**
	 * Start the migration process of the given Persistable class.
	 * 
	 * @param persistableClass
	 *            The Persistable being migrated.
	 * @param properties
	 *            A list of properties that will driven the migration.
	 * 
	 * @return A enumeration
	 * 
	 * @throws FloggyException
	 *             DOCUMENT ME!
	 */
	public abstract Enumeration start(Class persistableClass,
			Hashtable properties) throws FloggyException;
}
