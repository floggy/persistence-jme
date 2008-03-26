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

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Patient;

public class PatientForm extends Form implements CommandListener {

    Patient paciente;

    ObjectSet formacoes;

    TextField txtNome;

    TextField txtCPF;

    DateField dtNascimento;

    TextField txtEndereco;

    ChoiceGroup cgConvenio;

    TextField txtTelefoneCasa;

    TextField txtTelefoneCelular;

    TextField txtTelefoneTrabalho;

    Command cmdOk;

    Command cmdCancelar;

    public PatientForm(Patient paciente) {
        super("Patient");

        this.paciente = paciente;

        iniciaComponentes();
    }

    private void iniciaComponentes() {
        this.txtNome = new TextField("Nome", paciente.getNome(), 30,
                TextField.ANY);
        this.append(this.txtNome);

        this.txtCPF = new TextField("CPF", paciente.getCpf(), 30, TextField.ANY);
        this.append(this.txtCPF);

        this.dtNascimento = new DateField("Data Nascimento", DateField.DATE);
        this.dtNascimento.setDate(paciente.getDataNascimento());
        this.append(this.dtNascimento);

        this.txtEndereco = new TextField("Endereço", paciente.getAddress(),
                100, TextField.ANY);
        this.append(txtEndereco);

        this.append("Telefones");
        this.txtTelefoneCasa = new TextField("Casa", paciente.getTelefoneCasa(), 20, TextField.PHONENUMBER);
        this.append(this.txtTelefoneCasa);
        this.txtTelefoneCelular = new TextField("Celular", paciente.getTelefoneCelular(), 20, TextField.PHONENUMBER);
        this.append(this.txtTelefoneCelular);
        this.txtTelefoneTrabalho = new TextField("Trabalho", paciente.getTelefoneTrabalho(), 20, TextField.PHONENUMBER);
        this.append(this.txtTelefoneTrabalho);

        this.cgConvenio = new ChoiceGroup("Tipo:", ChoiceGroup.EXCLUSIVE);
        this.cgConvenio.append("Particular", null);
        this.cgConvenio.append("Convênio", null);
        this.cgConvenio.setSelectedIndex(0, paciente.isInsuredByGoverment());
        this.cgConvenio.setSelectedIndex(1, !paciente.isInsuredByGoverment());
        this.append(cgConvenio);
        
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
                this.paciente.setNome(this.txtNome.getString());
                this.paciente.setCpf(this.txtCPF.getString());
                this.paciente.setDataNascimento(this.dtNascimento.getDate());
                this.paciente.setInsuredByGoverment(this.cgConvenio.isSelected(0));
                this.paciente.setTelefoneCasa(this.txtTelefoneCasa.getString());
                this.paciente.setTelefoneCelular(this.txtTelefoneCelular.getString());
                this.paciente.setTelefoneTrabalho(this.txtTelefoneTrabalho.getString());
                pm.save(this.paciente);

            } catch (FloggyException e) {
            	HospitalMIDlet.showException(e);
            }
        }
        HospitalMIDlet.setCurrent(new PatientList());
    }
}