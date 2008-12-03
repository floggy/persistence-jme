package net.sourceforge.floggy.persistence.bug2168632;

import net.sourceforge.floggy.persistence.Persistable;

public interface ModelElement extends Persistable {
    
    void setName(String name);

}
