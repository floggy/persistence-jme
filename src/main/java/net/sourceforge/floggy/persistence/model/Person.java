/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
