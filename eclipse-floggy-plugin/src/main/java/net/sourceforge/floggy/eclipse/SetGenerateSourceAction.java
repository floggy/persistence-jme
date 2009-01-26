package net.sourceforge.floggy.eclipse;

import org.eclipse.core.runtime.QualifiedName;

public class SetGenerateSourceAction extends AbstractSetPropertyAction {

	public static final QualifiedName PROPERTY_NAME = new QualifiedName(
			Activator.PLUGIN_ID, "generateSource");

	public SetGenerateSourceAction() {
		propertyName = PROPERTY_NAME;
	}

}
