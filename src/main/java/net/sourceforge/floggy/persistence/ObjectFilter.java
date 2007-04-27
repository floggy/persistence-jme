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
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 * @author Thiago Rossato <thiagorossato@users.sourceforge.net>
 * @since 1.0
 */
class ObjectFilter implements RecordFilter {

	private final Persistable persistable;

	private Filter filter;

	ObjectFilter(Persistable persistable, Filter filter) {
		this.persistable = persistable;
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
