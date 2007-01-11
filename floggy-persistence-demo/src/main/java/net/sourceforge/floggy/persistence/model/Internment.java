/*
 * Criado em 20/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.model;

import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

public class Internment implements Persistable {
    
    Date dtEntrada;
    
    Date dtSaida;    
    
    String motivo;
    
    Patient paciente;
    
    Doctor medico;
    
    Bed leito;

    public Date getDtEntrada() {
        return dtEntrada;
    }

    public void setDtEntrada(Date dtEntrada) {
        this.dtEntrada = dtEntrada;
    }

    public Date getDtSaida() {
        return dtSaida;
    }

    public void setDtSaida(Date dtSaida) {
        this.dtSaida = dtSaida;
    }

    public Bed getLeito() {
        return leito;
    }

    public void setLeito(Bed leito) {
        this.leito = leito;
    }

    public Doctor getMedico() {
        return medico;
    }

    public void setMedico(Doctor medico) {
        this.medico = medico;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Patient getPaciente() {
        return paciente;
    }

    public void setPaciente(Patient paciente) {
        this.paciente = paciente;
    }
       
}
