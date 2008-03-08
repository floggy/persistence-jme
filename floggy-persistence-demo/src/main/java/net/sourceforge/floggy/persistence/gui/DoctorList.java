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

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Doctor;

public class DoctorList extends List implements CommandListener {
    ObjectSet medicos;

    Command cmdInserir;

    Command cmdAlterar;

    Command cmdExcluir;

    Command cmdVoltar;

    public DoctorList() {
        super("Lista de Médicos", List.IMPLICIT);

        iniciaDados();
        iniciaComponentes();
    }

    private void iniciaDados() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();

            medicos = pm.find(Doctor.class, null, new Comparator() {
                public int compare(Persistable arg0, Persistable arg1) {
                    String s1 = arg0 == null ? "" : ((Doctor) arg0).getNome();
                    String s2 = arg1 == null ? "" : ((Doctor) arg1).getNome();

                    return s1.compareTo(s2);
                }

            });

            for (int i = 0; i < medicos.size(); i++) {
                Doctor medico = (Doctor) medicos.get(i);
                this.append(medico.getNome() + " - CRM " + medico.getCrm(),
                        null);
            }

        } catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
        }
    }

    private void iniciaComponentes() {
        this.cmdVoltar = new Command("Voltar", Command.BACK, 0);
        this.addCommand(this.cmdVoltar);

        this.cmdInserir = new Command("Inserir", Command.ITEM, 1);
        this.addCommand(this.cmdInserir);

        this.cmdAlterar = new Command("Alterar", Command.ITEM, 2);
        this.addCommand(this.cmdAlterar);

        this.cmdExcluir = new Command("Excluir", Command.ITEM, 3);
        this.addCommand(this.cmdExcluir);

        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdVoltar) {
            MainForm mainForm = new MainForm();
            HospitalMIDlet.setCurrent(mainForm);
        } else if (cmd == this.cmdInserir) {
            Doctor medico = new Doctor();
            HospitalMIDlet.setCurrent(new DoctorForm(medico));
        } else if (cmd == this.cmdAlterar) {
            if (this.getSelectedIndex() != -1) {
                Doctor medico = null;

                try {
                    medico = (Doctor) medicos.get(this.getSelectedIndex());
                    HospitalMIDlet.setCurrent(new DoctorForm(medico));
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        } else if (cmd == this.cmdExcluir) {
            if (this.getSelectedIndex() != -1) {

                try {
                    Doctor medico = (Doctor) medicos.get(this
                            .getSelectedIndex());
                    PersistableManager.getInstance().delete(medico);
                    this.iniciaDados();
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        }
    }
}
