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

import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Formation;
import net.sourceforge.floggy.persistence.model.Doctor;

public class DoctorForm extends Form implements CommandListener {

    Doctor medico;

    ObjectSet formacoes;

    TextField txtNome;

    TextField txtCPF;

    DateField dtNascimento;

    TextField txtCRM;

    ChoiceGroup cgFormacao;

    Command cmdOk;

    Command cmdCancelar;

    public DoctorForm(Doctor medico) {
        super("Médico");

        this.medico = medico;

        iniciaComponentes();

        iniciaFormacao();
    }

    private void iniciaComponentes() {
        this.txtNome = new TextField("Nome", medico.getNome(), 30,
                TextField.ANY);
        this.append(this.txtNome);

        this.txtCPF = new TextField("CPF", medico.getCpf(), 30, TextField.ANY);
        this.append(this.txtCPF);

        this.dtNascimento = new DateField("Data Nascimento", DateField.DATE);
        this.dtNascimento.setDate(medico.getDataNascimento());
        this.append(this.dtNascimento);

        this.txtCRM = new TextField("CRM", medico.getCrm(), 30, TextField.ANY);
        this.append(this.txtCRM);

        this.cgFormacao = new ChoiceGroup("Formação", ChoiceGroup.MULTIPLE);
        this.append(cgFormacao);

        this.cmdOk = new Command("Ok", Command.OK, 0);
        this.addCommand(this.cmdOk);

        this.cmdCancelar = new Command("Cancelar", Command.CANCEL, 1);
        this.addCommand(this.cmdCancelar);

        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdOk) {
            PersistableManager pm = PersistableManager.getInstance();

            try {
                this.medico.setNome(this.txtNome.getString());
                this.medico.setCpf(this.txtCPF.getString());
                this.medico.setCrm(this.txtCRM.getString());
                this.medico.setDataNascimento(this.dtNascimento.getDate());

                if(this.medico.getFormacoes() != null) {
                    this.medico.getFormacoes().removeAllElements();
                }
                else {
                    this.medico.setFormacoes(new Vector());
                }
                for (int i = 0; i < this.cgFormacao.size(); i++) {
                    if (this.cgFormacao.isSelected(i) ) {
                        this.medico.getFormacoes().addElement(this.formacoes.get(i));                        
                    } 
                }

                pm.save(this.medico);

            } catch (FloggyException e) {
            	HospitalMIDlet.showException(e);
            }
        }
        HospitalMIDlet.setCurrent(new DoctorList());
    }

    public void iniciaFormacao() {
       
        PersistableManager pm = PersistableManager.getInstance();
        try {
            formacoes = pm.find(Formation.class, null, new Comparator() {

                public int compare(Persistable arg0, Persistable arg1) {
                    Formation f1 = (Formation) arg0;
                    Formation f2 = (Formation) arg1;

                    return f1.getFormacao().compareTo(f2.getFormacao());
                }

            });

            for (int i = 0; i < formacoes.size(); i++) {
                Formation formacao = (Formation) formacoes.get(i);
                int index = this.cgFormacao.append(formacao.getFormacao(), null);
                if ((medico.getFormacoes() != null) && (this.medico.getFormacoes().contains(formacao))) {
                    this.cgFormacao.setSelectedIndex(index, true);
                }

            }

        } catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
        }

    }
}