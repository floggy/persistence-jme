package net.sourceforge.floggy.eclipse;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.apache.commons.logging.Log;

public interface RuntimeCollector {

	public abstract void setSource(IFolder source);

	public abstract void run(IProgressMonitor monitor) throws CoreException;
	
	public abstract void setEclipseProperties(IJavaProject javaProject, Log log, IFolder floggyTemp);

}