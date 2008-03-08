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

import java.util.Date;

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
import net.sourceforge.floggy.persistence.model.BedComparator;
import net.sourceforge.floggy.persistence.model.Internment;
import net.sourceforge.floggy.persistence.model.Bed;
import net.sourceforge.floggy.persistence.model.Doctor;
import net.sourceforge.floggy.persistence.model.Patient;

public class InternmentForm extends Form implements CommandListener {

    Internment internacao;

    ObjectSet pacientes;

    ObjectSet medicos;

    ObjectSet leitos;

    DateField dtEntrada;

    TextField txtMotivo;

    ChoiceGroup cgPacientes;

    ChoiceGroup cgMedicos;

    ChoiceGroup cgLeitos;

    Command cmdOk;

    Command cmdCancelar;

    public InternmentForm() {
        super("Internação");

        this.internacao = new Internment();
        
        iniciaComponentes();

        iniciaPacientes();

        iniciaMedicos();

        iniciaLeitos();

    }

    private void iniciaComponentes() {
        this.dtEntrada = new DateField("Data Entrada", DateField.DATE);
        this.dtEntrada.setDate(new Date());
        this.append(this.dtEntrada);

        this.txtMotivo = new TextField("Motivo", internacao.getMotivo(), 100,
                TextField.ANY);
        this.append(this.txtMotivo);

        this.cgPacientes = new ChoiceGroup("Patient", ChoiceGroup.EXCLUSIVE);
        this.append(cgPacientes);

        this.cgMedicos = new ChoiceGroup("Doctor", ChoiceGroup.EXCLUSIVE);
        this.append(cgMedicos);

        this.cgLeitos = new ChoiceGroup("Leitos", ChoiceGroup.EXCLUSIVE);
        this.append(cgLeitos);

        this.cmdOk = new Command("Ok", Command.OK, 0);
        this.addCommand(this.cmdOk);

        this.cmdCancelar = new Command("Cancelar", Command.CANCEL, 1);
        this.addCommand(this.cmdCancelar);

        this.setCommandListener(this);
    }

    public void iniciaPacientes() {
        PersistableManager pm = PersistableManager.getInstance();
        try {
            pacientes = pm.find(Patient.class, null, new Comparator() {

                public int compare(Persistable arg0, Persistable arg1) {
                    Patient p1 = (Patient) arg0;
                    Patient p2 = (Patient) arg1;

                    return p1.getNome().compareTo(p2.getNome());
                }

            });
            for (int i = 0; i < pacientes.size(); i++) {
                Patient paciente = (Patient) pacientes.get(i);
                this.cgPacientes.append(paciente.getNome(), null);
            }

        } catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
        }

    }

    public void iniciaMedicos() {
        PersistableManager pm = PersistableManager.getInstance();
        try {
            medicos = pm.find(Doctor.class, null, new Comparator() {

                public int compare(Persistable arg0, Persistable arg1) {
                    Doctor p1 = (Doctor) arg0;
                    Doctor p2 = (Doctor) arg1;

                    return p1.getNome().compareTo(p2.getNome());
                }

            });

            for (int i = 0; i < medicos.size(); i++) {
                Doctor medico = (Doctor) medicos.get(i);
                this.cgMedicos.append(medico.getNome(), null);
            }

        } catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
        }

    }

    public void iniciaLeitos() {
        PersistableManager pm = PersistableManager.getInstance();
        try {
            leitos = pm.find(Bed.class, null, new BedComparator());
            for (int i = 0; i < leitos.size(); i++) {
                Bed leito = (Bed) leitos.get(i);
                this.cgLeitos.append(String.valueOf(leito.getNumber()), null);
            }

        } catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
        }

    }

    public Patient getPacienteSelecionado() {
        for (int i = 0; i < this.cgPacientes.size(); i++) {
            if (this.cgPacientes.isSelected(i)) {
                try {
                    return (Patient) this.pacientes.get(i);
                } catch (Exception e) {
                    // 
                }

            }

        }
        return null;
    }
    
    public Doctor getMedicoSelecionado() {
        for (int i = 0; i < this.cgMedicos.size(); i++) {
            if (this.cgMedicos.isSelected(i)) {
                try {
                    return (Doctor) this.medicos.get(i);
                } catch (Exception e) {
                    // 
                }

            }

        }
        return null;
    }


    public Bed getLeitoSelecionado() {
        for (int i = 0; i < this.cgLeitos.size(); i++) {
            if (this.cgLeitos.isSelected(i)) {
                try {
                    return (Bed) this.leitos.get(i);
                } catch (Exception e) {
                    // 
                }

            }

        }
        return null;
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdOk) {
            PersistableManager pm = PersistableManager.getInstance();

            try {
                this.internacao.setDtEntrada(this.dtEntrada.getDate());
                this.internacao.setMotivo(this.txtMotivo.getString());
                this.internacao.setPaciente(getPacienteSelecionado());
                this.internacao.setMedico(getMedicoSelecionado());
                this.internacao.setLeito(getLeitoSelecionado());
                pm.save(this.internacao);
            } catch (FloggyException e) {
            	HospitalMIDlet.showException(e);
            }
        }
        HospitalMIDlet.setCurrent(new MainForm());
    }

}
