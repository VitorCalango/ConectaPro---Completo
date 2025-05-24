package com.conectapro.bean;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.log4j.Logger;

import com.conectapro.model.Usuario;
import com.conectapro.service.LoginService;
import com.conectapro.model.TipoUser;

@Named
@SessionScoped
public class LoginBean implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(LoginBean.class);
    
    @Inject
    private LoginService loginService;
    
    private String email;
    private String senha;
    private boolean lembrar;
    private Usuario usuarioLogado;
    private boolean unauthorized;
    
    public String login() {
        logger.debug("Tentativa de login para o email: " + email);
        
        try {
            usuarioLogado = loginService.autenticar(email, senha);
            
            if (usuarioLogado != null) {
                logger.info("Login bem-sucedido para o usuário: " + usuarioLogado.getEmail());
                
                // Limpa os campos de login
                senha = null;
                
                // Redireciona para a página inicial após login bem-sucedido
                return "/index.xhtml?faces-redirect=true";
            } else {
                logger.warn("Falha na autenticação para o email: " + email);
                FacesContext.getCurrentInstance().addMessage(null, 
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro de Autenticação", 
                                    "Email ou senha incorretos. Por favor, tente novamente."));
                return null;
            }
        } catch (Exception e) {
            logger.error("Erro durante o processo de login", e);
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", 
                                "Ocorreu um erro durante o login: " + e.getMessage()));
            return null;
        }
    }
    
    public String logout() {
        logger.debug("Realizando logout do usuário: " + (usuarioLogado != null ? usuarioLogado.getEmail() : "desconhecido"));
        
        // Invalida a sessão
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        
        usuarioLogado = null;
        email = null;
        senha = null;
        lembrar = false;
        
        logger.info("Logout realizado com sucesso");
        
        // Redireciona para a página inicial após logout
        return "/index.xhtml?faces-redirect=true";
    }
    
    public boolean isLogado() {
        return usuarioLogado != null;
    }
    
    // Getters e Setters
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSenha() {
        return senha;
    }
    
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    public boolean isLembrar() {
        return lembrar;
    }
    
    public void setLembrar(boolean lembrar) {
        this.lembrar = lembrar;
    }
    
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }
    
    public void setUsuarioLogado(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
    }
    
    public boolean isUnauthorized() {
        return unauthorized;
    }
    
    public void setUnauthorized(boolean unauthorized) {
        this.unauthorized = unauthorized;
    }
    
    public void checkUnauthorized() {
        if (unauthorized) {
            logger.warn("Tentativa de acesso não autorizado detectada");
            FacesContext.getCurrentInstance().addMessage(null, 
                new FacesMessage(FacesMessage.SEVERITY_WARN, "Acesso Restrito", 
                                "Você precisa estar logado para acessar esta página."));
            unauthorized = false; // Reset para não mostrar a mensagem novamente após o redirecionamento
        }
    }
    
    public String getTipoUsuario() {
        if (usuarioLogado == null) {
            return null;
        }
        return usuarioLogado.getTipo().toString();
    }
    
    public boolean isPrestador() {
        return usuarioLogado != null && usuarioLogado.getTipo() == TipoUser.PRESTADOR;
    }
    
    public boolean isCliente() {
        return usuarioLogado != null && usuarioLogado.getTipo() == TipoUser.CLIENTE;
    }
    
    /**
     * Retorna a URL da foto de perfil do usuário logado ou uma imagem padrão se não houver.
     * 
     * @return URL da foto de perfil ou URL da imagem padrão
     */
    public String getProfileImageUrl() {
        if (usuarioLogado == null) {
            return null;
        }
        
        if (usuarioLogado.getFotoUrl() != null && !usuarioLogado.getFotoUrl().isEmpty()) {
            return usuarioLogado.getFotoUrl();
        } else {
            // Retorna a URL de uma imagem padrão baseada no tipo de usuário
            if (usuarioLogado.getTipo() == TipoUser.PRESTADOR) {
                return "/resources/images/default-provider-avatar.svg";
            } else {
                return "/resources/images/default-client-avatar.svg";
            }
        }
    }
}
