package net.sourceforge.floggy.persistence.pool;

import java.io.IOException;
import java.net.URL;

public interface InputPool {

    public int getFileCount();

    public String getFileName(int index);

    public URL getFileURL(int index) throws IOException;

}
