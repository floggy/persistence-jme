package net.sourceforge.floggy.maven;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class ZipUtils {

    public static void unzip(File file, File directory) throws IOException {
	ZipFile zipFile = new ZipFile(file);
	Enumeration entries = zipFile.entries();

	if (!directory.exists() && !directory.mkdirs()) {
	    throw new IOException("Unable to create the " + directory
		    + " directory!");
	}
	while (entries.hasMoreElements()) {
	    File temp;
	    ZipEntry entry = (ZipEntry) entries.nextElement();
	    if (entry.isDirectory()) {
		temp = new File(directory, entry.getName());
		if (!temp.exists() && !temp.mkdirs()) {
		    throw new IOException("Unable to create the " + temp
			    + " directory!");
		}
	    } else {
		temp = new File(directory, entry.getName());
		IOUtils.copy(zipFile.getInputStream(entry),
			new FileOutputStream(temp));
	    }

	}
	zipFile.close();
    }

    public static void zip(File[] directories, File output) throws IOException {
	FileInputStream in = null;
	ZipOutputStream out = null;
	ZipEntry entry = null;

	Collection allFiles = new LinkedList();
	for (int i = 0; i < directories.length; i++) {
	    allFiles.addAll(FileUtils.listFiles(directories[i], null, true));
	}

	out = new ZipOutputStream(new BufferedOutputStream(
		new FileOutputStream(output)));
	for (Iterator iter = allFiles.iterator(); iter.hasNext();) {
	    File temp = (File) iter.next();
	    String name = getEntryName(directories, temp.getAbsolutePath());
	    entry = new ZipEntry(name);
	    out.putNextEntry(entry);
	    if (!temp.isDirectory()) {
		in = new FileInputStream(temp);
		IOUtils.copy(in, out);
		in.close();
	    }

	}

	out.close();
    }

    private static String getEntryName(File[] directories, String file) {
	for (int i = 0; i < directories.length; i++) {
	    String temp = directories[i].getAbsolutePath();
	    int index = file.indexOf(temp);
	    if (index != -1) {
		return file.substring(temp.length() + 1);

	    }
	}
	return null;
    }

}