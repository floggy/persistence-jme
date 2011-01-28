/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.persistence.model;

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

public class Person implements Persistable {

    protected String passport;

    protected String name;

    protected Date bornDate;

    protected transient int age;
    
    public Person() {
	}

    public Person(String passport, String nome, Date dataNascimento) {
        setPassport(passport);
        setName(nome);
        setBornDate(dataNascimento);
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public void setBornDate(Date dataNascimento) {
        this.bornDate = dataNascimento;
        if (dataNascimento != null) {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c2.setTime(dataNascimento);
            this.age = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
        } else {
            this.age = 0;
        }

    }

    public int getAge() {
        return age;
    }

    public String getName() {
        return name;
    }

    public void setName(String nome) {
        this.name = nome;
    }

}
