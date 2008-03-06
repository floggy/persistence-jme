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
import net.sourceforge.floggy.persistence.model.Formation;

public class FormationList extends List implements CommandListener, Comparator {

    ObjectSet formacoes;

    Command cmdInserir;

    Command cmdAlterar;

    Command cmdExcluir;

    Command cmdVoltar;

    public FormationList() {
        super("Lista de Formações", List.IMPLICIT);

        iniciaDados();
        iniciaComponentes();
    }

    private void iniciaDados() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();

            formacoes = pm.find(Formation.class, null, this);
            for (int i = 0; i < formacoes.size(); i++) {
                Formation element = (Formation) formacoes.get(i);
                this.append(element.getFormacao(), null);
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
            Formation formacao = new Formation();
            HospitalMIDlet.setCurrent(new FormationForm(formacao));
        } else if (cmd == this.cmdAlterar) {
            if (this.getSelectedIndex() != -1) {
                Formation formacao = null;

                try {
                    formacao = (Formation) formacoes
                            .get(this.getSelectedIndex());
                    HospitalMIDlet.setCurrent(new FormationForm(formacao));
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        } else if (cmd == this.cmdExcluir) {
            if (this.getSelectedIndex() != -1) {

                try {
                    Formation formacao = (Formation) formacoes.get(this
                            .getSelectedIndex());
                    PersistableManager.getInstance().delete(formacao);
                    this.iniciaDados();
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        }
    }

    public int compare(Persistable p1, Persistable p2) {
        Formation f1 = (Formation) p1;
        Formation f2 = (Formation) p2;

        return f1.getFormacao().compareTo(f2.getFormacao());
    }
}
