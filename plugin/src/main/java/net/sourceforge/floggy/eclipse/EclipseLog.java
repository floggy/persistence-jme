package net.sourceforge.floggy.eclipse;

import org.apache.commons.logging.impl.NoOpLog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.console.ConsolePlugin;

public class EclipseLog extends NoOpLog{

	public EclipseLog() {
		super();
	}

	public EclipseLog(String name) {
		super(name);
	}

	@Override
	public void debug(Object message) {
		log(IStatus.INFO, message);
	}

	@Override
	public void debug(Object message, Throwable t) {
		log(IStatus.INFO, message, t);
	}

	@Override
	public void warn(Object message) {
		log(IStatus.WARNING, message);
	}

	@Override
	public void warn(Object message, Throwable t) {
		log(IStatus.WARNING, message, t);
	}

	@Override
	public void error(Object message) {
		log(IStatus.ERROR, message);
	}
	
	@Override
	public void error(Object message, Throwable t) {
		log(IStatus.ERROR, message, t);
	}
	
	@Override
	public void info(Object message, Throwable t) {
		log(IStatus.INFO, message, t);
	}
	
	@Override
	public void info(Object message) {
		log(IStatus.INFO, message);
	}
	
	protected void log(int severity, Object message) {
		log(severity, message, null);
	}
	
	protected void log(int severity, Object message, Throwable throwable) {
		IStatus status;
		if (throwable == null) {
			status= new Status(severity, Activator.PLUGIN_ID, (String)message);
		} else {
			status= new Status(severity, Activator.PLUGIN_ID, (String)message, throwable);
		}
		ConsolePlugin.log(status);
	}
	

}
