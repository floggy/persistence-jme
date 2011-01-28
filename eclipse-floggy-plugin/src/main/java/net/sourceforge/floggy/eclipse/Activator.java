/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.eclipse;

import java.util.Enumeration;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import org.eclipse.ui.plugin.AbstractUIPlugin;

import org.osgi.framework.BundleContext;

/**
* The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {
	private static Activator plugin;
	private static BundleContext context;
	public static final String PLUGIN_ID = "net.sourceforge.floggy";

/**
   * The constructor
   */
	public Activator() {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param path DOCUMENT ME!
	* @param filePattern DOCUMENT ME!
	* @param recurse DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static Enumeration findEntries(String path, String filePattern,
		boolean recurse) {
		Enumeration result = null;

		if (context != null) {
			result = context.getBundle().findEntries(path, filePattern, recurse);
		}

		return result;
	}

	/**
	* Returns the shared instance
	*
	* @return the shared instance
	*/
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static boolean isMTJAvailble() {
		IExtensionRegistry registry = RegistryFactory.getRegistry();
		IExtensionPoint extension =
			registry.getExtensionPoint("org.eclipse.mtj.core.mtjbuildhook");

		return extension != null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	/**
	 * DOCUMENT ME!
	*
	* @param context DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void start(BundleContext context) throws Exception {
		super.start(context);

		Activator.plugin = this;
		Activator.context = context;

		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		workspace.addResourceChangeListener(new ConfigurationFileResourceListener(),
			IResourceChangeEvent.POST_CHANGE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	/**
	 * DOCUMENT ME!
	*
	* @param context DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		Activator.context = null;
		super.stop(context);
	}
}
