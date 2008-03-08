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