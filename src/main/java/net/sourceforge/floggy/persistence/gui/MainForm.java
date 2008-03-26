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
import javax.microedition.lcdui.List;

import net.sourceforge.floggy.persistence.HospitalMIDlet;

public class MainForm extends List implements CommandListener {

    Command cmdSair;

    public MainForm() {
        super("Hospital", List.IMPLICIT);

        iniciaComponentes();
    }

    protected void cadastrosIniciais() {
    }

    protected void iniciaComponentes() {
        this.append("Cadastrar Formação", null);
        this.append("Cadastrar Bed", null);
        this.append("Cadastrar Médico", null);
        this.append("Cadastrar Patient", null);
        this.append("-----", null);
        this.append("Incluir Internação", null);
        this.append("Autorizar Alta", null);        
        this.append("-----", null);
        this.append("Relatório de Leitos Livres", null);
        this.append("Relatório de Internações por Médicos", null);        
        
        this.cmdSair = new Command("Sair", Command.ITEM, 3);
        this.addCommand(this.cmdSair);

        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdSair) {
            HospitalMIDlet.exit();   
        } else if (cmd == List.SELECT_COMMAND) {
            switch (this.getSelectedIndex()) {
            case 0:
                HospitalMIDlet.setCurrent(new FormationList());
                break;
            case 1:
                HospitalMIDlet.setCurrent(new BedList());
                break;
            case 2:
                HospitalMIDlet.setCurrent(new DoctorList());
                break;
            case 3:
        		HospitalMIDlet.setCurrent(new PatientList());
        		break;
        	case 5:
                HospitalMIDlet.setCurrent(new InternmentForm());
                break;
            case 6:
                HospitalMIDlet.setCurrent(new InternmentList());
                break;
            case 8:
                HospitalMIDlet.setCurrent(new FreeBedsReport());
                break;                
            }            
        }
    }
}
