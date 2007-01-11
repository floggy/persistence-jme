/*
 * Criado em 13/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.model;

import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;

public class Doctor extends Person implements Persistable {

    String crm;

    Vector formacoes;

    public Doctor() {
        //
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        this.crm = crm;
    }

    public Vector getFormacoes() {
        return formacoes;
    }

    public void setFormacoes(Vector formacoes) {
        this.formacoes = formacoes;
    }
}
