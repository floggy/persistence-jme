/*
 * Criado em 13/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.model;

import net.sourceforge.floggy.persistence.Persistable;

public class Formation implements Persistable {

    String formacao;

    public Formation() {
        
    }
    
    public Formation(String formacao) {
        this.formacao = formacao;
    }

    public String getFormacao() {
        return formacao;
    }

    public void setFormacao(String formacao) {
        this.formacao = formacao;
    }
    
    public boolean equals(Object object) {
        if (object instanceof Formation) {
            Formation formacao = (Formation) object;
            return this.formacao.equals(formacao.getFormacao());
        }
        
        return false;
    }
}