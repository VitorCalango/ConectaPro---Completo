package com.conectapro.model;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;

@Entity
@DiscriminatorValue("PRESTADOR")
public class Prestador extends Usuario {
    private static final long serialVersionUID = 1L;
    
    @Column(length = 18)
    private String cnpj;
    
    private String areaAtuacao;
    
    @ElementCollection
    @CollectionTable(name = "prestador_especialidades", joinColumns = @JoinColumn(name = "prestador_id"))
    @Column(name = "especialidade")
    private List<String> especialidades;
    
    private Integer anosExperiencia;
    
    @Column(length = 1000)
    private String certificacoes;
    
    private Double avaliacao;
    
    private Integer totalServicos;
    
    private Integer totalAvaliacoes;
    
    @OneToMany(mappedBy = "prestador", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Servico> servicos;
    
    public Prestador() {
        super();
        this.avaliacao = 0.0;
        this.totalServicos = 0;
        this.totalAvaliacoes = 0;
    }
    
    @Override
    public TipoUser getTipo() {
        return TipoUser.PRESTADOR;
    }
    
    public String getCnpj() {
        return cnpj;
    }
    
    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public String getAreaAtuacao() {
        return areaAtuacao;
    }
    
    public void setAreaAtuacao(String areaAtuacao) {
        this.areaAtuacao = areaAtuacao;
    }
    
    public List<String> getEspecialidades() {
        return especialidades;
    }
    
    public void setEspecialidades(List<String> especialidades) {
        this.especialidades = especialidades;
    }
    
    public Integer getAnosExperiencia() {
        return anosExperiencia;
    }
    
    public void setAnosExperiencia(Integer anosExperiencia) {
        this.anosExperiencia = anosExperiencia;
    }
    
    public String getCertificacoes() {
        return certificacoes;
    }
    
    public void setCertificacoes(String certificacoes) {
        this.certificacoes = certificacoes;
    }
    
    public Double getAvaliacao() {
        return avaliacao;
    }
    
    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }
    
    public Integer getTotalServicos() {
        return totalServicos;
    }
    
    public void setTotalServicos(Integer totalServicos) {
        this.totalServicos = totalServicos;
    }
    
    public Integer getTotalAvaliacoes() {
        return totalAvaliacoes;
    }
    
    public void setTotalAvaliacoes(Integer totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }
    
    public List<Servico> getServicos() {
        return servicos;
    }
    
    public void setServicos(List<Servico> servicos) {
        this.servicos = servicos;
    }
    
    @Override
    public String toString() {
        return "Prestador [areaAtuacao=" + areaAtuacao + ", anosExperiencia=" + anosExperiencia + 
               ", avaliacao=" + avaliacao + ", " + super.toString() + "]";
    }
}
