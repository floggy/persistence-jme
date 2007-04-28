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

	private final Persistable p;

	private final Filter f;

	ObjectFilter(Persistable p, Filter f) {
		this.p = p;
		this.f = f;
	}

	public boolean matches(byte[] b) {
		try {
			((__Persistable) this.p).__load(b);
		} catch (Exception e) {
			// Ignore
		}
		return f.matches(p);
	}
}
