package com.conectapro.service;

import java.io.Serializable;
import javax.inject.Inject;
import org.apache.log4j.Logger;

import com.conectapro.model.Usuario;
import com.conectapro.model.dao.LoginDao;

public class LoginService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(LoginService.class);
    
    @Inject
    private LoginDao loginDao;
    
    /**
     * Autentica um usuário com base no email e senha fornecidos.
     * 
     * @param email O email do usuário
     * @param senha A senha do usuário
     * @return O objeto Usuario se a autenticação for bem-sucedida, ou null caso contrário
     */
    public Usuario autenticar(String email, String senha) {
        logger.debug("Tentando autenticar usuário com email: " + email);
        
        if (email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty()) {
            logger.warn("Tentativa de login com email ou senha vazios");
            return null;
        }
        
        try {
            // Busca o usuário pelo email
            Usuario usuario = loginDao.buscarPorEmail(email);
            
            // Verifica se o usuário existe e se a senha está correta
            if (usuario != null && senha.equals(usuario.getSenha())) {
                logger.info("Autenticação bem-sucedida para o usuário: " + email);
                return usuario;
            } else {
                logger.warn("Autenticação falhou para o usuário: " + email);
                return null;
            }
        } catch (Exception e) {
            logger.error("Erro ao autenticar usuário", e);
            throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
    }
}
