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

import net.sourceforge.floggy.persistence.Persistable;

public class Patient extends Person implements Persistable {

    private static int TELEFONE_CASA = 0;

    private static int TELEFONE_CELULAR = 1;

    private static int TELEFONE_TRABALHO = 2;

    private String address;

    private String[] phones;

    private boolean insuredByGoverment;

    public Patient() {
        this.phones = new String[3];
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String endereco) {
        this.address = endereco;
    }

    public boolean isInsuredByGoverment() {
        return insuredByGoverment;
    }

    public void setInsuredByGoverment(boolean particular) {
        this.insuredByGoverment = particular;
    }
    
    public String[] getPhones() {
		return phones;
	}
    
    public void setPhones(String[] telefones) {
		this.phones = telefones;
	}

    public String getTelefoneCasa() {
        return this.phones[TELEFONE_CASA];
    }

    public void setTelefoneCasa(String telefone) {
        this.phones[TELEFONE_CASA] = telefone;
    }

    public String getTelefoneCelular() {
        return this.phones[TELEFONE_CELULAR];
    }

    public void setTelefoneCelular(String telefone) {
        this.phones[TELEFONE_CELULAR] = telefone;
    }

    public String getTelefoneTrabalho() {
        return this.phones[TELEFONE_TRABALHO];
    }

    public void setTelefoneTrabalho(String telefone) {
        this.phones[TELEFONE_TRABALHO] = telefone;
    }

}
