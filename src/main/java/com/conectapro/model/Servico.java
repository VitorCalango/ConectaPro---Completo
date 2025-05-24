package com.conectapro.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
public class Servico implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titulo;
    
    @Column(length = 2000)
    private String descricao;
    
    private String categoria;
    
    private BigDecimal precoBase;
    
    private Boolean precoNegociavel;
    
    private Boolean disponivel;
    
    @ElementCollection
    @CollectionTable(name = "servico_imagens", joinColumns = @JoinColumn(name = "servico_id"))
    @Column(name = "url_imagem")
    private List<String> imagens = new ArrayList<>();
    
    @ManyToOne
    @JoinColumn(name = "prestador_id")
    private Prestador prestador;
    
    @OneToMany(mappedBy = "servico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Avaliacao> avaliacoes = new ArrayList<>();
    
    private Double mediaAvaliacoes;
    
    private Integer totalAvaliacoes;
    
    public Servico() {
        this.mediaAvaliacoes = 0.0;
        this.totalAvaliacoes = 0;
        this.disponivel = true;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitulo() {
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getCategoria() {
        return categoria;
    }
    
    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }
    
    public BigDecimal getPrecoBase() {
        return precoBase;
    }
    
    public void setPrecoBase(BigDecimal precoBase) {
        this.precoBase = precoBase;
    }
    
    public Boolean getPrecoNegociavel() {
        return precoNegociavel;
    }
    
    public void setPrecoNegociavel(Boolean precoNegociavel) {
        this.precoNegociavel = precoNegociavel;
    }
    
    public Boolean getDisponivel() {
        return disponivel;
    }
    
    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }
    
    public List<String> getImagens() {
        return imagens;
    }
    
    public void setImagens(List<String> imagens) {
        this.imagens = imagens;
    }
    
    public void adicionarImagem(String urlImagem) {
        this.imagens.add(urlImagem);
    }
    
    public void removerImagem(String urlImagem) {
        this.imagens.remove(urlImagem);
    }
    
    public Prestador getPrestador() {
        return prestador;
    }
    
    public void setPrestador(Prestador prestador) {
        this.prestador = prestador;
    }
    
    public List<Avaliacao> getAvaliacoes() {
        return avaliacoes;
    }
    
    public void setAvaliacoes(List<Avaliacao> avaliacoes) {
        this.avaliacoes = avaliacoes;
    }
    
    public Double getMediaAvaliacoes() {
        return mediaAvaliacoes;
    }
    
    public void setMediaAvaliacoes(Double mediaAvaliacoes) {
        this.mediaAvaliacoes = mediaAvaliacoes;
    }
    
    public Integer getTotalAvaliacoes() {
        return totalAvaliacoes;
    }
    
    public void setTotalAvaliacoes(Integer totalAvaliacoes) {
        this.totalAvaliacoes = totalAvaliacoes;
    }
    
    public void adicionarAvaliacao(Avaliacao avaliacao) {
        this.avaliacoes.add(avaliacao);
        this.totalAvaliacoes++;
        
        // Recalcula a média de avaliações
        double soma = 0;
        for (Avaliacao a : avaliacoes) {
            soma += a.getNota();
        }
        this.mediaAvaliacoes = soma / this.totalAvaliacoes;
        
        // Atualiza a avaliação do prestador também
        if (this.prestador != null) {
            this.prestador.setTotalAvaliacoes(this.prestador.getTotalAvaliacoes() + 1);
            
            double somaPrestador = 0;
            for (Servico s : this.prestador.getServicos()) {
                somaPrestador += s.getMediaAvaliacoes() * s.getTotalAvaliacoes();
            }
            this.prestador.setAvaliacao(somaPrestador / this.prestador.getTotalAvaliacoes());
        }
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Servico other = (Servico) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "Servico [id=" + id + ", titulo=" + titulo + ", categoria=" + categoria + 
               ", prestador=" + (prestador != null ? prestador.getNome() : "null") + "]";
    }
}
