package net.sourceforge.floggy.persistence.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.PersistableMetadata;
import net.sourceforge.floggy.persistence.model.Formation;

public class MainForm extends List implements CommandListener {

    Command cmdSair;

    public MainForm() {
        super("Hospital", List.IMPLICIT);

        iniciaComponentes();
    }

    protected void cadastrosIniciais() {
        PersistableManager pm = PersistableManager.getInstance();

        // Formacaoes
        try {
            PersistableMetadata metadata = pm.getMetadata(Formation.class);

            if (metadata.getObjectCount() == 0) {
                pm.save(new Formation("Pediatra"));
                pm.save(new Formation("Ginecologista"));
                pm.save(new Formation("Clinico Geral"));
                pm.save(new Formation("Dermatologista"));
                pm.save(new Formation("Ortopedista"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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