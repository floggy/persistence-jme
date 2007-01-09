/*
 * Criado em 21/08/2005.
 *
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence;

import javax.microedition.rms.RecordFilter;

/**
 * An implementation of RecordComparator for comparing two objects.
 * 
 * @author Thiago Rossato
 * @since 1.0
 */
class ObjectFilter implements RecordFilter {

    private Persistable persistable;

    private Filter filter;

    ObjectFilter(Class persistableClass, Filter filter) throws FloggyException {
	try {
	    persistable = (Persistable) persistableClass.newInstance();
	} catch (Exception e) {
	    throw new FloggyException(e.getMessage());
	}

	this.filter = filter;
    }

    public boolean matches(byte[] buffer) {
	try {
	    ((__Persistable) this.persistable).__load(buffer);
	} catch (Exception e) {
	    // Ignore
	}
	return filter.matches(persistable);
    }
}
