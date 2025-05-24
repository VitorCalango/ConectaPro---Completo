package com.conectapro.bean;

import java.io.Serializable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;
import org.primefaces.model.file.UploadedFile;

import com.conectapro.model.Cliente;
import com.conectapro.model.Prestador;
import com.conectapro.model.TipoUser;
import com.conectapro.model.Usuario;
import com.conectapro.service.CadastroUsuarioService;
import com.conectapro.util.jpa.Transactional;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(CadastroUsuarioBean.class);
    
    @Inject
    private CadastroUsuarioService cadastroService;
    
    private Usuario usuario;
    private String confirmacaoSenha;
    private UploadedFile fotoPerfil;
    private String tipoSelecionado;
    
    @PostConstruct
    public void init() {
        logger.debug("Inicializando CadastroUsuarioBean");
        limpar();
    }
    
    private void limpar() {
        // Inicialmente não sabemos o tipo, então criamos um Cliente como padrão
        usuario = new Cliente();
        tipoSelecionado = "CLIENTE"; // Valor inicial
        confirmacaoSenha = null;
        fotoPerfil = null;
    }
    
    public void onTipoChange() {
        logger.debug("Tipo de usuário alterado para: " + tipoSelecionado);
        
        if (tipoSelecionado == null || tipoSelecionado.isEmpty()) {
            return;
        }
        
        // Guarda os dados comuns do usuário atual
        String nome = usuario != null ? usuario.getNome() : null;
        String sobrenome = usuario != null ? usuario.getSobrenome() : null;
        String email = usuario != null ? usuario.getEmail() : null;
        String senha = usuario != null ? usuario.getSenha() : null;
        String telefone = usuario != null ? usuario.getTelefone() : null;
        String cidade = usuario != null ? usuario.getCidade() : null;
        String estado = usuario != null ? usuario.getEstado() : null;
        String cep = usuario != null ? usuario.getCep() : null;
        String descricao = usuario != null ? usuario.getDescricao() : null;
        String fotoUrl = usuario != null ? usuario.getFotoUrl() : null;
        String fotoCapaUrl = usuario != null ? usuario.getFotoCapaUrl() : null;
        String instagram = usuario != null ? usuario.getInstagram() : null;
        String facebook = usuario != null ? usuario.getFacebook() : null;
        String whatsapp = usuario != null ? usuario.getWhatsapp() : null;
        
        // Cria a instância correta baseada no tipo selecionado
        if ("CLIENTE".equals(tipoSelecionado)) {
            if (!(usuario instanceof Cliente)) {
                Cliente cliente = new Cliente();
                // Copia os dados comuns
                copiarPropriedadesComuns(nome, sobrenome, email, senha, telefone, cidade, estado, cep, 
                                        descricao, fotoUrl, fotoCapaUrl, instagram, facebook, whatsapp, cliente);
                usuario = cliente;
                logger.debug("Criada nova instância de Cliente");
            }
        } else if ("PRESTADOR".equals(tipoSelecionado)) {
            if (!(usuario instanceof Prestador)) {
                Prestador prestador = new Prestador();
                // Copia os dados comuns
                copiarPropriedadesComuns(nome, sobrenome, email, senha, telefone, cidade, estado, cep, 
                                        descricao, fotoUrl, fotoCapaUrl, instagram, facebook, whatsapp, prestador);
                usuario = prestador;
                logger.debug("Criada nova instância de Prestador");
            }
        }
        
        logger.debug("Usuário agora é instância de: " + usuario.getClass().getSimpleName());
    }
    
    @Transactional
    public void cadastrar() {
        try {
            if (!validarSenha()) {
                return;
            }
            
            // Garantir que o tipo de usuário foi selecionado
            if (tipoSelecionado == null || tipoSelecionado.isEmpty()) {
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Selecione o tipo de usuário."));
                return;
            }
            
            // Garantir que a instância do usuário corresponde ao tipo selecionado
            onTipoChange();
            
            // Processa o upload da foto, se houver
            if (fotoPerfil != null && fotoPerfil.getContent() != null && fotoPerfil.getContent().length > 0) {
                String caminhoFoto = processarUploadFoto();
                usuario.setFotoUrl(caminhoFoto);
            }
            
            // Salva o usuário
            cadastroService.salvar(usuario);
            
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", "Cadastro realizado com sucesso."));
            
            logger.info("Usuário cadastrado com sucesso: " + usuario.getEmail());
            
            limpar();
        } catch (Exception e) {
            logger.error("Erro ao cadastrar usuário", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "Não foi possível realizar o cadastro. " + e.getMessage()));
        }
    }
    
    private boolean validarSenha() {
        if (usuario.getSenha() == null || !usuario.getSenha().equals(confirmacaoSenha)) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "As senhas não conferem."));
            return false;
        }
        
        if (usuario.getSenha().length() < 8) {
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro!", "A senha deve ter pelo menos 8 caracteres."));
            return false;
        }
        
        return true;
    }
    
    private void copiarPropriedadesComuns(String nome, String sobrenome, String email, String senha, 
                                    String telefone, String cidade, String estado, String cep, 
                                    String descricao, String fotoUrl, String fotoCapaUrl, 
                                    String instagram, String facebook, String whatsapp, Usuario destino) {
        destino.setNome(nome);
        destino.setSobrenome(sobrenome);
        destino.setEmail(email);
        destino.setSenha(senha);
        destino.setTelefone(telefone);
        destino.setCidade(cidade);
        destino.setEstado(estado);
        destino.setCep(cep);
        destino.setDescricao(descricao);
        destino.setFotoUrl(fotoUrl);
        destino.setFotoCapaUrl(fotoCapaUrl);
        destino.setInstagram(instagram);
        destino.setFacebook(facebook);
        destino.setWhatsapp(whatsapp);
    }
    
    private void copiarPropriedadesComuns(Usuario origem, Usuario destino) {
        copiarPropriedadesComuns(
            origem.getNome(),
            origem.getSobrenome(),
            origem.getEmail(),
            origem.getSenha(),
            origem.getTelefone(),
            origem.getCidade(),
            origem.getEstado(),
            origem.getCep(),
            origem.getDescricao(),
            origem.getFotoUrl(),
            origem.getFotoCapaUrl(),
            origem.getInstagram(),
            origem.getFacebook(),
            origem.getWhatsapp(),
            destino
        );
    }
    
    private String processarUploadFoto() {
        try {
            // Gera um nome único para o arquivo usando timestamp e nome original
            String nomeArquivo = System.currentTimeMillis() + "_" + fotoPerfil.getFileName();
            
            // Define o caminho virtual (URL) para acesso à imagem
            String caminhoVirtual = "/resources/images/profiles/" + nomeArquivo;
            
            // Define o caminho físico onde o arquivo será salvo
            String caminhoReal = FacesContext.getCurrentInstance().getExternalContext()
                    .getRealPath("/resources/images/profiles/");
            
            // Garante que o caminho termine com separador de diretório
            if (!caminhoReal.endsWith(System.getProperty("file.separator"))) {
                caminhoReal += System.getProperty("file.separator");
            }
            
            // Caminho completo do arquivo
            String arquivoCompleto = caminhoReal + nomeArquivo;
            
            logger.debug("Salvando imagem em: " + arquivoCompleto);
            
            // Cria o arquivo no sistema de arquivos
            java.io.File arquivo = new java.io.File(arquivoCompleto);
            
            // Garante que o diretório pai existe
            arquivo.getParentFile().mkdirs();
            
            // Salva o conteúdo do arquivo
            java.io.OutputStream outputStream = new java.io.FileOutputStream(arquivo);
            outputStream.write(fotoPerfil.getContent());
            outputStream.close();
            
            logger.info("Imagem salva com sucesso: " + nomeArquivo);
            
            return caminhoVirtual;
        } catch (Exception e) {
            logger.error("Erro ao salvar imagem de perfil", e);
            throw new RuntimeException("Erro ao processar imagem: " + e.getMessage(), e);
        }
    }
    
    // Getters e Setters
    
    public Usuario getUsuario() {
        return usuario;
    }
    
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public String getConfirmacaoSenha() {
        return confirmacaoSenha;
    }
    
    public void setConfirmacaoSenha(String confirmacaoSenha) {
        this.confirmacaoSenha = confirmacaoSenha;
    }
    
    public UploadedFile getFotoPerfil() {
        return fotoPerfil;
    }
    
    public void setFotoPerfil(UploadedFile fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }
    
    public String getTipoSelecionado() {
        return tipoSelecionado;
    }
    
    public void setTipoSelecionado(String tipoSelecionado) {
        this.tipoSelecionado = tipoSelecionado;
    }
}
