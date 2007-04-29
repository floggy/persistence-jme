/*
 * Criado em 21/08/2005.
 *
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence;

import javax.microedition.rms.RecordFilter;

/**
 * An internal implementation of <code>RecordComparator</code> for comparing two objects.
 * 
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
