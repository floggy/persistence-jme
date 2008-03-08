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
import net.sourceforge.floggy.persistence.model.Bed;

public class BedForm extends Form implements CommandListener {
    
    Bed leito;
    
    TextField txtNumeroLeito;
    
    TextField txtAndarLeito;
    
    Command cmdOk;
    
    Command cmdCancelar;
    
    public BedForm(Bed leito) {
        super("Bed");
        
        this.leito = leito;
        
        iniciaComponentes();
    }
    
    private void iniciaComponentes() {
        this.txtNumeroLeito = new TextField("Número do Bed", String.valueOf(leito.getNumber()), 3, TextField.NUMERIC);
        this.append(this.txtNumeroLeito);

        this.txtAndarLeito  = new TextField("Andar do Bed", String.valueOf(leito.getFloor()), 15, TextField.NUMERIC);
        this.append(this.txtAndarLeito);        
               
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
                leito.setNumber(Integer.parseInt(this.txtNumeroLeito.getString()));
                leito.setFloor(Integer.parseInt(this.txtAndarLeito.getString()));
                pm.save(leito);         
            
            } catch (FloggyException e) {
            	HospitalMIDlet.showException(e);
            } 
        }
        
        HospitalMIDlet.setCurrent(new BedList());
    }
}
