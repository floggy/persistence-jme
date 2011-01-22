/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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

package net.sourceforge.floggy.eclipse.builder;

import javassist.ClassPool;
import javassist.NotFoundException;
import net.sourceforge.floggy.eclipse.FloggyNature;
import net.sourceforge.floggy.eclipse.SetAddDefaultConstructorAction;
import net.sourceforge.floggy.eclipse.SetGenerateSourceAction;
import net.sourceforge.floggy.persistence.Configuration;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

public abstract class AbstractBuilder {
	
	public static final String PERSISTABLE_CLASS_NAME = "net.sourceforge.floggy.persistence.Persistable";

	public static final String RECORDSTORE_CLASS_NAME = "javax.microedition.rms.RecordStore";

	public abstract IProject[] build(IProject project, IProgressMonitor monitor) throws Exception;
	
	protected void cleanFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
		IResource[] members = folder.members();
		for (int i = 0; i < members.length; i++) {
			IResource member = members[i];
			if (member.getType()==IResource.FOLDER){
				cleanFolder((IFolder) member, monitor);
			} else if (member.getType()==IResource.FILE) {
				((IFile)member).delete(true, monitor);
			}
		}
	}
	
	protected void copyFiles(IFolder source, IFolder destination, IProgressMonitor monitor) throws CoreException {
		IFolder newDestination;
		IFile targetFile;
		IFile sourceFile;
		source.refreshLocal(IResource.DEPTH_ONE, monitor);
		IResource[] resources = source.members();
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.getType() == IResource.FOLDER){
				String folderName = resource.getFullPath().lastSegment();
				newDestination = destination.getFolder(folderName);
				if (!newDestination.exists()){
					newDestination.create(IResource.DERIVED, true, monitor);
				}
				copyFiles((IFolder) resource, newDestination, monitor);
			} else if (resource.getType()==IResource.FILE) {
				sourceFile = (IFile) resource;
				targetFile = destination.getFile(sourceFile.getName());
				if (targetFile.exists()){
					targetFile.setContents(sourceFile.getContents(), IResource.DERIVED, monitor);
				} else {
					targetFile.create(sourceFile.getContents(), IResource.DERIVED, monitor);
				}
			}
		}	
	}

	protected Configuration createWeaverConfiguration(IProject project) throws CoreException {
		
		String addDefaultConstructor = project.getPersistentProperty(SetAddDefaultConstructorAction.PROPERTY_NAME);
		String generateSource = project.getPersistentProperty(SetGenerateSourceAction.PROPERTY_NAME);
				
		Configuration configuration= new Configuration();

		configuration.setAddDefaultConstructor(Boolean.valueOf(addDefaultConstructor).booleanValue());
		configuration.setGenerateSource(Boolean.valueOf(generateSource).booleanValue());

		return configuration;
	}

	protected boolean validateClasspath(IProject project, ClassPool classPool) throws CoreException {
		boolean valid = true;

		try {
			classPool.get(PERSISTABLE_CLASS_NAME);
		} catch (NotFoundException e) {
			valid = false;
			IMarker marker = project.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.MESSAGE, "You must to add the Floggy framework library to the build path.");
			marker.setAttribute(IMarker.SOURCE_ID, FloggyNature.NATURE_ID);
		}

		try {
			classPool.get(RECORDSTORE_CLASS_NAME);
		} catch (NotFoundException e) {
			valid = false;
			IMarker marker = project.createMarker(IMarker.PROBLEM);
			marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
			marker.setAttribute(IMarker.MESSAGE, "You must to add the MIDP library to the build path.");
			marker.setAttribute(IMarker.SOURCE_ID, FloggyNature.NATURE_ID);
		}

		if (valid) {
			IMarker[] markers = project.findMarkers(IMarker.PROBLEM, false, IResource.DEPTH_ZERO);
			for (int i = 0; i < markers.length; i++) {

				Object sourceId = markers[i].getAttribute(IMarker.SOURCE_ID);
				if (FloggyNature.NATURE_ID.equals(sourceId)) {
					markers[i].delete();
				}
			}
		}

		return valid; 
	}

}
