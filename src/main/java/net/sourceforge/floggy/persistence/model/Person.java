/*
 * Criado em 13/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.model;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

public class Person implements Persistable {

    String cpf;

    String nome;

    Date dataNascimento;

    transient int idade;

    public Person() {
        //
    }

    public Person(String cpf, String nome, Date dataNascimento) {
        setCpf(cpf);
        setNome(nome);
        setDataNascimento(dataNascimento);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
        if (dataNascimento != null) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c2.setTime(dataNascimento);
            this.idade = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        } else {
            this.idade = 0;
        }

    }

    public int getIdade() {
        return idade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
