package net.sourceforge.floggy.persistence.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Bed;
import net.sourceforge.floggy.persistence.model.BedComparator;

public class BedList extends List implements CommandListener {

    ObjectSet leitos;

    Command cmdInserir;

    Command cmdAlterar;

    Command cmdExcluir;

    Command cmdVoltar;

    public BedList() {
        super("Lista de Leitos", List.IMPLICIT);

        iniciaDados();
        iniciaComponentes();
    }

    private void iniciaDados() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();

            leitos = pm.find(Bed.class, null, new BedComparator());

            for (int i = 0; i < leitos.size(); i++) {
                Bed leito = (Bed) leitos.get(i);
                this.append(leito.getNumber() + " - Andar " + leito.getFloor(), null);
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
            Bed leito = new Bed();
            HospitalMIDlet.setCurrent(new BedForm(leito));
        } else if (cmd == this.cmdAlterar) {
            if (this.getSelectedIndex() != -1) {
                Bed leito = null;

                try {
                    leito = (Bed) leitos.get(this.getSelectedIndex());
                    HospitalMIDlet.setCurrent(new BedForm(leito));
                } catch (FloggyException e) {
                    e.printStackTrace();
                }
            }
        } else if (cmd == this.cmdExcluir) {
            if (this.getSelectedIndex() != -1) {

                try {
                    Bed leito = (Bed) leitos.get(this.getSelectedIndex());
                    PersistableManager.getInstance().delete(leito);
                    this.iniciaDados();
                } catch (FloggyException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}