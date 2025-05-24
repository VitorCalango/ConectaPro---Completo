package com.conectapro.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@DiscriminatorValue("CLIENTE")
public class Cliente extends Usuario {
    private static final long serialVersionUID = 1L;
    
    @Column(length = 14)
    private String cpf;
    
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    
    @Column(length = 1000)
    private String preferencias;
    
    public Cliente() {
        super();
    }
    
    @Override
    public TipoUser getTipo() {
        return TipoUser.CLIENTE;
    }
    
    public String getCpf() {
        return cpf;
    }
    
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
    
    public Date getDataNascimento() {
        return dataNascimento;
    }
    
    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
    
    public String getPreferencias() {
        return preferencias;
    }
    
    public void setPreferencias(String preferencias) {
        this.preferencias = preferencias;
    }
    
    @Override
    public String toString() {
        return "Cliente [cpf=" + cpf + ", " + super.toString() + "]";
    }
}
