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
