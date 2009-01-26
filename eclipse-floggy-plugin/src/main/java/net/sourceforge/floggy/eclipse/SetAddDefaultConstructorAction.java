package net.sourceforge.floggy.eclipse;

import org.eclipse.core.runtime.QualifiedName;

public class SetAddDefaultConstructorAction extends AbstractSetPropertyAction {

	public static final QualifiedName PROPERTY_NAME = new QualifiedName(
			Activator.PLUGIN_ID, "addDefaultConstructor");

	public SetAddDefaultConstructorAction() {
		propertyName = PROPERTY_NAME;
	}
}
