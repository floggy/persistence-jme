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
package net.sourceforge.floggy.persistence.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Formation;

public class FormationForm extends Form implements CommandListener {
    
    Formation formacao;
    
    TextField txtFormacao;
    
    Command cmdOk;
    
    Command cmdCancelar;
    
    public FormationForm(Formation formacao) {
        super("Formação");
        
        this.formacao = formacao;
        
        iniciaComponentes();
    }
    
    private void iniciaComponentes() {
        this.txtFormacao = new TextField("Formação", formacao.getFormacao(), 30, TextField.ANY);
        this.append(this.txtFormacao);
        
        this.cmdOk = new Command("Ok", Command.OK, 0);
        this.addCommand(this.cmdOk);
        
        this.cmdCancelar = new Command("Cancelar", Command.CANCEL, 1);
        this.addCommand(this.cmdCancelar);
        
        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if(cmd == this.cmdOk) {
            PersistableManager pm = PersistableManager.getInstance();
            
            try {
                formacao.setFormacao(this.txtFormacao.getString());
                pm.save(formacao);
            } catch (FloggyException e) {
            	HospitalMIDlet.showException(e);
            }
        }
        
        HospitalMIDlet.setCurrent(new FormationList());
    }
}
