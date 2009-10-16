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
 * A class that helps the developer migrate from a previous version of its
 * application to a new version. <br>
 * The general use of it follows:<br>
 * <br>
 * 
 * <code>
 * MigrationManager manager = MigrationManager.getInstance();<br>
 * String[] notMigratedClasses = manager.getNotMigratedClasses();<br>
 * <br>
 * for (int i = 0; i < notMigratedClasses.length; i++) {<br><br>
 * &nbsp;if (notMigratedClasses[i].equals("net.sourceforge.floggy.Person")) {<br>
 * &nbsp;&nbsp;//does a quickMigration because the classes didn't changed between versions<br>
 * &nbsp;&nbsp;manager.quickMigration(Class.forName(notMigratedClasses[i]));<br>
 * &nbsp;}<br><br>
 * &nbsp;if (notMigratedClasses[i].equals("net.sourceforge.floggy.Phone")) {<br>
 * &nbsp;&nbsp;Enumeration enumeration = manager.start(Class.forName(notMigratedClasses[i]), null);<br>
 * &nbsp;&nbsp;while (enumeration.hasMoreElements()) {<br>
 * &nbsp;&nbsp;&nbsp;Hashtable fields = enumeration.nextElement();<br>
 * &nbsp;&nbsp;&nbsp;Integer countryCode = fields.get("countryCode");<br>
 * &nbsp;&nbsp;&nbsp;if (countryCode == null) {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;enumeration.delete();<br>
 * &nbsp;&nbsp;&nbsp;} else {<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;Phone phone = new Phone();<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;phone.setNumber(fields.get("number"));<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;phone.setType(Phone.GENERAL);<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;enumeration.update(phone);<br>
 * &nbsp;&nbsp;&nbsp;}<br>
 * &nbsp;&nbsp;}<br>
 * &nbsp;}<br>
 * }
 * </code>
 * 
 * @author Thiago Moreira
 * 
 * @since 1.3.0
 */
public abstract class MigrationManager {

	/**
	 * It enables the developer to iterate over all registers without update or
	 * delete it.
	 */
	public static final String ITERATION_MODE = "ITERATION_MODE";
	/**
	 * It drives the migration avoiding or not the loading of Persistable fields
	 */
	public static final String LAZY_LOAD = "LAZY_LOAD";
	/**
	 * It enables the developer to migrate from a version earlier than 1.3.0
	 */
	public static final String MIGRATE_FROM_PREVIOUS_1_3_0_VERSION = "MIGRATE_FROM_PREVIOUS_1_3_0_VERSION";

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
	 *            The Persistable being migrated.
	 * 
	 * @throws FloggyException
	 *             If the enumeration wasn't whole processed.
	 */
	public abstract void finish(Class persistableClass) throws FloggyException;

	/**
	 * Retrieves a list with all classes that needs to be migrated.
	 * 
	 * @return a String array that contains a list of classes that must be
	 *         migrated.
	 */
	public abstract String[] getNotMigratedClasses();

	/**
	 * Execute a quick migration in classes that didn't changed between
	 * versions.
	 * 
	 * @param persistableClass
	 *            The Persistable being migrated.
	 * 
	 * @throws FloggyException
	 *             A exception that holds the underling problem.
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
	 *             A exception that holds the underling problem.
	 */
	public abstract Enumeration start(Class persistableClass,
			Hashtable properties) throws FloggyException;
}
