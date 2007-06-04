/*
 * Criado em 20/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.gui;

import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Internment;

public class InternmentList extends List implements CommandListener {
    ObjectSet internacoes;

    Command cmdAlta;

    Command cmdVoltar;

    public InternmentList() {
        super("Interna��es", List.IMPLICIT);

        iniciaDados();
        iniciaComponentes();
    }

    private void iniciaDados() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();

            internacoes = pm.find(Internment.class, new Filter() {

                public boolean matches(Persistable arg0) {
                    return ((Internment) arg0).getDtSaida() == null;
                }

            }, new Comparator() {
                public int compare(Persistable arg0, Persistable arg1) {
                    String s1 = ((Internment) arg0).getPaciente().getNome();
                    String s2 = ((Internment) arg1).getPaciente().getNome();

                    return s1.compareTo(s2);
                    
                }
            });

            for (int i = 0; i < internacoes.size(); i++) {
                Internment element = (Internment) internacoes.get(i);
                this.append(element.getPaciente().getNome() + " - "
                        + element.getLeito().getNumber(), null);
            }

        } catch (FloggyException e) {
            e.printStackTrace();
        }
    }

    private void iniciaComponentes() {
        this.cmdVoltar = new Command("Voltar", Command.BACK, 0);
        this.addCommand(this.cmdVoltar);

        this.cmdAlta = new Command("Alta", Command.ITEM, 1);
        this.addCommand(this.cmdAlta);

        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdVoltar) {
            MainForm mainForm = new MainForm();
            HospitalMIDlet.setCurrent(mainForm);
        } else if (cmd == this.cmdAlta) {
            if (this.getSelectedIndex() != -1) {
                try {
                    Internment internacao = (Internment) internacoes.get(this
                            .getSelectedIndex());
                    internacao.setDtSaida(new Date());
                    PersistableManager.getInstance().save(internacao);
                    this.iniciaDados();
                } catch (FloggyException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}