package net.sourceforge.floggy.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sourceforge.floggy.persistence.Weaver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class FloggyBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "net.sourceforge.floggy.floggyBuilder";
	
	/**
	 *
	 * 
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		boolean generateSource = true;
		try {
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", EclipseLog.class.getName());
			IProject project= getProject();
			Weaver weaver = new Weaver();
			IJavaProject javaProject = JavaCore.create(project);
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);
			List<String> list = new ArrayList<String>();
			for (IClasspathEntry classpathEntry : entries) {
				list.add(classpathEntry.getPath().toOSString());
			}
			IPath root= project.getLocation(); 
			File input= root.removeLastSegments(1).append(javaProject.getOutputLocation()).toFile();
			
			File temp = new File(root.toFile(), String
					.valueOf(System.currentTimeMillis()));
			FileUtils.forceMkdir(temp);
			weaver.setOutputFile(temp);
			weaver.setInputFile(input);
			weaver.setClasspath(list.toArray(new String[0]));
			weaver.setGenerateSource(generateSource);
			weaver.execute();
			FileUtils.copyDirectory(temp, input);
			FileUtils.forceDelete(temp);
			
		} catch (Exception e) {
			IStatus status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
			throw new CoreException(status);
		}
		return null;
	}
}
