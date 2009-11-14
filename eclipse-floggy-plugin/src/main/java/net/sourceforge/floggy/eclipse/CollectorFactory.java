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

package net.sourceforge.floggy.eclipse;

import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

public class CollectorFactory {

	private static final String MTJ_BUILD_HOOK_EXT_ID = "org.eclipse.mtj.core.mtjbuildhook";
	private static boolean useJarPackager;
	private static RuntimeCollector collector;

	public static RuntimeCollector createCollector() {
		if (collector == null) {
			try {
				IExtensionRegistry reg = RegistryFactory.getRegistry();
				IExtensionPoint ext = reg.getExtensionPoint(MTJ_BUILD_HOOK_EXT_ID);
				if (ext != null) {
					useJarPackager = true;
				}
			} catch (Exception e) {
				useJarPackager = false;
			}
			if (useJarPackager) {
				collector = new JarPackager();
			} else {
				collector = new NoOpCollector();
			}
		}
		return collector;
	}
}
