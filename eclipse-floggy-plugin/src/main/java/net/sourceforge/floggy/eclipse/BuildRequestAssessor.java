package net.sourceforge.floggy.eclipse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.JavaCore;

/**
 * Decides if a build is really needed.
 * <p>
 * This is not as trivial as it might seem as eclipse automatic builds have a
 * habit of seemingly happening without any good reason (e.g. somtimes it can
 * just happen even if you don't change anything)
 * <p>
 * This has resulted in infinite loops in the past (when using Pulsar / MTJ).
 * This class assesses the resources which have changed and make a more informed
 * decision.
 * 
 * @author <a href="mailto:dan.murphy@floggy.org">Dan Murphy</a>
 * 
 */
public class BuildRequestAssessor {

	private static final Log LOG = LogFactory.getLog(BuildRequestAssessor.class);
	private static final String MJT_TEMP_DIR = ".mtj.tmp/";
	private static final String CLASS_SUFFIX = ".class";

	/**
	 * Determines if a build of the project is required. If we're being asked be
	 * involved in a {@link IncrementalProjectBuilder.FULL_BUILD} or a
	 * {@link IncrementalProjectBuilder.CLEAN_BUILD} then a build is required.
	 * <p>
	 * Things are a little more complex in the case of an
	 * {@link IncrementalProjectBuilder.INCREMENTAL_BUILD} or a
	 * {@link IncrementalProjectBuilder.AUTO_BUILD}, in which case it is
	 * necessary to look at the resource delta (what has changed since the last
	 * build) in order to decide if a Floggy build is necessary. The main reason
	 * for this is that Mobile Java Tools or Pulsar can, when in combination
	 * with Floggy, result in an infinite build loop because both believe that a
	 * change made by the other builder means a build is required.
	 * 
	 * @param project
	 * @param buildKind
	 *            what type of build has been requested
	 *            {@link IncrementalProjectBuilder}
	 * @param resouceDelta
	 *            if null then it is assumed that the changes cannot be
	 *            determined and so a build is needed
	 * @return whether a build is required
	 * @throws CoreException
	 *             if something goes badly wrong when determining what natures
	 *             the given project has
	 */
	public boolean isBuildRequired(IProject project, int buildKind,
			IResourceDelta resouceDelta) throws CoreException {
		boolean result = false;
		if (!project.hasNature(JavaCore.NATURE_ID)
				|| !project.hasNature(FloggyNature.NATURE_ID)) {
			LOG.warn(project.getName() + " isn't a java floggy project");
			return false;
		}
		switch (buildKind) {
		case IncrementalProjectBuilder.INCREMENTAL_BUILD:
			LOG.debug("Incremental build of " + resouceDelta);
			break;
		case IncrementalProjectBuilder.AUTO_BUILD:
			LOG.debug("Automatic build of " + resouceDelta);
			break;
		case IncrementalProjectBuilder.FULL_BUILD:
			LOG.debug("Full build invoked");
			result = true;
			break;
		case IncrementalProjectBuilder.CLEAN_BUILD:
			LOG.debug("Clean build invoked");
			result = true;
			break;
		default:
			return false;
		}

		if (resouceDelta == null) {
			LOG.debug("Cannot determine which resources have changed");
			result = true;
		} else {
			logDelta(resouceDelta);
			result = isBuildRequired(resouceDelta);
		}
		LOG.info((result ? "Floggy build required"
				: "Floggy build not required"));
		return result;
	}

	/**
	 * Determines if, given a resource delta a build is required.
	 * <p>
	 * By now we only care if the resource delta contains a .class file that is
	 * not in the MTJ temporary directory (MJT_TEMP_DIR) as this is where it
	 * places pre-verified classes.
	 * 
	 * @param delta
	 * @return
	 * @throws CoreException
	 */
	private boolean isBuildRequired(IResourceDelta delta) throws CoreException {
		boolean buildNeeded = false;
		IResourceDelta[] changes = delta.getAffectedChildren();
		IResource resource;
		String pathAsString;
		for (int i = 0; i < changes.length; i++) {
			resource = changes[i].getResource();
			if (resource.getType() == IResource.FILE) {
				pathAsString = resource.getProjectRelativePath()
						.toPortableString();
				if (pathAsString.endsWith(CLASS_SUFFIX)
						&& !pathAsString.startsWith(MJT_TEMP_DIR)) {
					buildNeeded = true;
					LOG.debug("Build required due to change of "
							+ resource.getFullPath().toPortableString());
				}
			} else if (resource.getType() == IResource.FOLDER) {
				buildNeeded = isBuildRequired(changes[i]);
				if (buildNeeded) {
					break;
				}
			}
		}
		return buildNeeded;
	}

	private void logDelta(IResourceDelta projectDelta) {
		StringBuffer buffer = new StringBuffer();
		logDelta(projectDelta, buffer);
		LOG.debug(buffer.toString());
	}

	private void logDelta(IResourceDelta delta, StringBuffer buf) {
		if (delta != null) {
			int flags = delta.getFlags();
			if (flags != 0) {
				appendToBuffer(buf, delta);
			}
			IResourceDelta[] children = delta.getAffectedChildren();
			if (children != null) {
				for (int i = 0; i < children.length; i++) {
					logDelta(children[i], buf);
				}
			}
		}
	}

	private void appendToBuffer(StringBuffer buf, IResourceDelta delta) {
		buf.append("Resource: " + delta.getResource().getFullPath());
		buf.append(' ');
		appendType(buf, delta.getResource().getType());
		buf.append(" ( ");
		appendKind(buf, delta.getKind());
		buf.append(' ');
		appendFlags(buf, delta.getFlags());
		buf.append(")\n");
	}

	private void appendType(StringBuffer buf, int type) {
		buf.append("Type: ");
		switch (type) {
		case IResource.FILE:
			buf.append("file");
			break;
		case IResource.FOLDER:
			buf.append("folder");
			break;
		case IResource.PROJECT:
			buf.append("project");
			break;
		case IResource.ROOT:
			buf.append("root");
			break;
		}
	}

	private void appendKind(StringBuffer buf, int kind) {
		buf.append("delta kind: ");
		switch (kind) {
		case IResourceDelta.ADDED:
			buf.append("added");
			break;
		case IResourceDelta.CHANGED:
			buf.append("changed");
			break;
		case IResourceDelta.REMOVED:
			buf.append("removed");
			break;
		case IResourceDelta.ADDED_PHANTOM:
			buf.append("added phantom");
			break;
		case IResourceDelta.REMOVED_PHANTOM:
			buf.append("removed phantom");
			break;
		default:
			break;
		}
	}

	private void appendFlags(StringBuffer buf, int flags) {
		buf.append("flags: ");
		if ((flags & IResourceDelta.CONTENT) != 0)
			buf.append("content ");
		if ((flags & IResourceDelta.ENCODING) != 0)
			buf.append("encoding ");
		if ((flags & IResourceDelta.DESCRIPTION) != 0)
			buf.append("description ");
		if ((flags & IResourceDelta.OPEN) != 0)
			buf.append("open ");
		if ((flags & IResourceDelta.SYNC) != 0)
			buf.append("sync ");
		if ((flags & IResourceDelta.MARKERS) != 0)
			buf.append("markers ");
		if ((flags & IResourceDelta.REPLACED) != 0)
			buf.append("replaced ");
		if ((flags & IResourceDelta.MOVED_TO) != 0)
			buf.append("moved_to ");
		if ((flags & IResourceDelta.MOVED_FROM) != 0)
			buf.append("moved_from ");
		if ((flags & IResourceDelta.COPIED_FROM) != 0)
			buf.append("copied_from ");
	}

}