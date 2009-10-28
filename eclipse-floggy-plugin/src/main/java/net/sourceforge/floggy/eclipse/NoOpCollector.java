package net.sourceforge.floggy.eclipse;

import org.apache.commons.logging.Log;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;

public class NoOpCollector implements RuntimeCollector {

	public void run(IProgressMonitor monitor) throws CoreException {
		// TODO Auto-generated method stub

	}

	public void setEclipseProperties(IJavaProject javaProject, Log log, IFolder floggyTemp) {
		// TODO Auto-generated method stub

	}

	public void setSource(IFolder source) {
		// TODO Auto-generated method stub

	}

}
