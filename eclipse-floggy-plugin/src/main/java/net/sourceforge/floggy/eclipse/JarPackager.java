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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.AbstractList;
import java.util.Iterator;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.zip.CRC32;
import java.util.zip.ZipEntry;

import org.apache.commons.logging.Log;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

public class JarPackager implements RuntimeCollector {

	private static final String PREFERRED_FLOGGY_FOLDER = "lib";
	private static final String FLOGGY_JAR_NAME = "floggy-weaver-custom-classes.jar";

	private IJavaProject javaProject;
	private IFolder floggyTemp;
	private IFolder source;
	private IFile resultingJar;
	private Vector entries;
	private Vector byteData;
	private String packageFilter = "net/sourceforge/floggy/persistence/impl";
	private Log log;

	/* (non-Javadoc)
	 * @see net.sourceforge.floggy.eclipse.RuntimeCollector#setSource(org.eclipse.core.resources.IFolder)
	 */
	public void setSource(IFolder source) {
		this.source = source;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.floggy.eclipse.RuntimeCollector#setFilter(java.lang.String)
	 */
	public void setFilter(String packageFilter) {
		this.packageFilter = packageFilter;
	}

	/* (non-Javadoc)
	 * @see net.sourceforge.floggy.eclipse.RuntimeCollector#run(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void run(IProgressMonitor monitor) throws CoreException {
		monitor.subTask("Building jar");
		resultingJar = getOutputJar();
		if (resultingJar.exists()) {
			monitor.subTask("Removing existing jar");
			resultingJar.delete(true, false, monitor);
		}
		try {
			monitor.subTask("Writing jar");
			collectEntries();
			URI jarUri = resultingJar.getRawLocationURI();
			FileOutputStream out = new FileOutputStream(new File(jarUri));
			JarOutputStream jarOut = new JarOutputStream(new BufferedOutputStream(out));
			Iterator entries = getEntries().iterator();
			Iterator byteLists = getByteData().iterator();
			Vector byteList;
			Iterator byteListIterator;
			JarEntry jarEntry;
			while (entries.hasNext()) {
				jarEntry = (JarEntry) entries.next();
				jarOut.putNextEntry(jarEntry);
				byteList =  (Vector) byteLists.next();
				byteListIterator = byteList.iterator();
				byte[] byteData;
				while (byteListIterator.hasNext()) {
					byteData = (byte[]) byteListIterator.next();
					jarOut.write(byteData, 0, byteData.length);
				}
			}
			jarOut.close();
			exportJar(monitor);
		} catch (Exception e) {
			if (e instanceof CoreException) {
				throw (CoreException) e;
			} else {
				IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage(), e);
				throw new CoreException(status);
			}
		}
	}

	private AbstractList getByteData() {
		return byteData;
	}

	private AbstractList getEntries() {
		return entries;
	}

	private void collectEntries() throws CoreException, IOException {
		entries = new Vector();
		byteData = new Vector();
		collectEntries(source, entries, byteData);
	}

	private void collectEntries(IResource resource, Vector entries, Vector data) throws CoreException, IOException {
		if (resource.getType() == IResource.FOLDER) {
			IFolder folder = (IFolder) resource;
			IResource[] members = folder.members();
			for (int i = 0; i < members.length; i++) {
				collectEntries(members[i], entries, data);
			}
		} else if (resource.getType() == IResource.FILE) {
			IFile file = (IFile) resource;
			IPath path = file.getProjectRelativePath();
			String className = path.removeFirstSegments(1).toPortableString();
			if (className.startsWith(packageFilter)) {
				addEntry(className, file, entries, data);
			}

		}
	}

	private void addEntry(String className, IFile file, Vector entries, Vector data) throws CoreException, IOException {
		JarEntry jarEntry = new JarEntry(className);
		long length = new File(file.getLocationURI()).length();
		jarEntry.setMethod(ZipEntry.STORED);
		jarEntry.setSize(length);
		jarEntry.setCompressedSize(length);
		CRC32 crc = new CRC32();
		BufferedInputStream in = new BufferedInputStream(file.getContents());
		int len = 0;
		byte[] buffer = new byte[16384];
		Vector byteData = new Vector(1);
		while ((len = in.read(buffer)) > 0) {
			crc.update(buffer, 0 , len);
			byte[] bytes = new byte[len];
			System.arraycopy(buffer,0, bytes, 0, len);
			byteData.add(bytes);
		}
		data.add(byteData);
		jarEntry.setCrc(crc.getValue());
		in.close();
		entries.add(jarEntry);
	}
	
	private void addFloggyJarToClasspath(IFile jarFile, IProgressMonitor monitor, IClasspathEntry[] classPaths) throws CoreException {
		log.trace(">addFloggyJarToClasspath (" + jarFile + ", " + javaProject + ", " + monitor + ")");
		IClasspathEntry[] newClasspaths = new IClasspathEntry[classPaths.length + 1];
		System.arraycopy(classPaths, 0, newClasspaths, 0, classPaths.length);
		IClasspathEntry floggyEntry = JavaCore.newLibraryEntry(jarFile.getFullPath(), null, null, true);
		newClasspaths[classPaths.length] = floggyEntry;
		javaProject.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
		javaProject.setRawClasspath(newClasspaths, monitor);
	}
	
	private void exportJar(IProgressMonitor monitor) throws CoreException {
		log.trace(">processClasspath (" + floggyTemp + ", " + javaProject + ", " + monitor + ")");
		IClasspathEntry[] classPaths = javaProject.getRawClasspath();
		IClasspathEntry classPathEntry;
		IPath path;
		IFile weaverJar = getOutputJar();
		boolean found = false;
		for (int i = 0; i < classPaths.length; i++) {
			if (classPaths[i].getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				classPathEntry = JavaCore.getResolvedClasspathEntry(classPaths[i]);
				path = classPathEntry.getPath();
				if (path.lastSegment().equalsIgnoreCase(FLOGGY_JAR_NAME)) {
					found = true;
					break;
				}
			}
		}
		if (!found) {
			log.debug("Floggy custom weaver classes not on classpath, will add it");
			addFloggyJarToClasspath(weaverJar, monitor, classPaths);
		}
	}
	
	private IFile getOutputJar(){
		IFolder jarFolder = javaProject.getProject().getFolder(PREFERRED_FLOGGY_FOLDER);
		IFile jarFile;
		if (jarFolder.exists()) {
			jarFile = jarFolder.getFile(FLOGGY_JAR_NAME);
		} else {
			jarFile = javaProject.getProject().getFile(FLOGGY_JAR_NAME);
		}
		return jarFile;
	}

	public void setEclipseProperties(IJavaProject javaProject, Log log, IFolder floggyTemp) {
		this.javaProject = javaProject;
		this.log = log;
		this.floggyTemp = floggyTemp;
	};

}
