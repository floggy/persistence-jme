/*
 * Criado em 13/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.model;

import net.sourceforge.floggy.persistence.Persistable;

public class Patient extends Person implements Persistable {

    private static int TELEFONE_CASA = 0;

    private static int TELEFONE_CELULAR = 1;

    private static int TELEFONE_TRABALHO = 2;

    String endereco;

    String[] telefones;

    boolean particular;

    public Patient() {
        this.telefones = new String[3];
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public boolean isParticular() {
        return particular;
    }

    public void setParticular(boolean particular) {
        this.particular = particular;
    }

    public String getTelefoneCasa() {
        return this.telefones[TELEFONE_CASA];
    }

    public void setTelefoneCasa(String telefone) {
        this.telefones[TELEFONE_CASA] = telefone;
    }

    public String getTelefoneCelular() {
        return this.telefones[TELEFONE_CELULAR];
    }

    public void setTelefoneCelular(String telefone) {
        this.telefones[TELEFONE_CELULAR] = telefone;
    }

    public String getTelefoneTrabalho() {
        return this.telefones[TELEFONE_TRABALHO];
    }

    public void setTelefoneTrabalho(String telefone) {
        this.telefones[TELEFONE_TRABALHO] = telefone;
    }

}
