/*
 * Criado em 18/09/2005.
 * 
 * Todos os direiros reservados aos autores.
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
import net.sourceforge.floggy.persistence.model.Patient;

public class PatientList extends List implements CommandListener {
    ObjectSet pacientes;

    Command cmdInserir;

    Command cmdAlterar;

    Command cmdExcluir;

    Command cmdVoltar;

    public PatientList() {
        super("Lista de Pacientes", List.IMPLICIT);

        iniciaDados();
        iniciaComponentes();
        
    }

    private void iniciaDados() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();

            pacientes = pm.find(Patient.class, null, new Comparator() {
                public int compare(Persistable arg0, Persistable arg1) {
                    String s1 = arg0 == null ? "" : ((Patient) arg0).getNome();
                    String s2 = arg1 == null ? "" : ((Patient) arg1).getNome();

                    return s1.compareTo(s2);
                }
            });

            for (int i = 0; i < pacientes.size(); i++) {
                Patient paciente = (Patient) pacientes.get(i);
                String tipo;
                if (paciente.isInsuredByGoverment()) {
                    tipo = "Particular";
                } else {
                    tipo = "Convênio";
                }
                this.append(paciente.getNome() + " - " + tipo, null);
            }

        } catch (FloggyException e) {
            e.printStackTrace();
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
            Patient paciente = new Patient();
            HospitalMIDlet.setCurrent(new PatientForm(paciente));
        } else if (cmd == this.cmdAlterar) {
            if (this.getSelectedIndex() != -1) {
                Patient paciente = null;

                try {
                    paciente = (Patient) pacientes
                            .get(this.getSelectedIndex());
                    HospitalMIDlet.setCurrent(new PatientForm(paciente));
                } catch (FloggyException e) {
                    e.printStackTrace();
                }
            }
        } else if (cmd == this.cmdExcluir) {
            if (this.getSelectedIndex() != -1) {

                try {
                    Patient paciente = (Patient) pacientes.get(this
                            .getSelectedIndex());
                    PersistableManager.getInstance().delete(paciente);
                    this.iniciaDados();
                } catch (FloggyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}