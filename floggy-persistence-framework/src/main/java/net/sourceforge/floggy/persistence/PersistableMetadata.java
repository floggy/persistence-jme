package net.sourceforge.floggy.persistence;

public interface PersistableMetadata {
    /**
         * Returns the number of objects.
         * 
         * @return
         */
    public int getObjectCount();

    /**
         * Return the
         * 
         * @return
         */
    public long getLastModified();

    /**
         * 
         * @return
         */
    public int getSize();

    /**
         * 
         * @return
         */
    public int getVersion();
}
