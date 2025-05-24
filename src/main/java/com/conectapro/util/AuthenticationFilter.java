package com.conectapro.util;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.conectapro.bean.LoginBean;

/**
 * Filtro para controle de acesso a páginas protegidas.
 * Redireciona para a página de login se o usuário não estiver autenticado.
 */
@WebFilter(urlPatterns = {"/views/perfil-usuario.xhtml", "/views/divulgar-servico.xhtml"})
public class AuthenticationFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(AuthenticationFilter.class);
    
    @Inject
    private LoginBean loginBean;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Inicialização do filtro
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Verifica se o usuário está logado
        if (loginBean != null && loginBean.isLogado()) {
            // Usuário autenticado, continua a requisição
            logger.debug("Usuário autenticado acessando página protegida: " + httpRequest.getRequestURI());
            chain.doFilter(request, response);
        } else {
            // Usuário não autenticado, redireciona para a página de login
            logger.warn("Tentativa de acesso a página protegida sem autenticação: " + httpRequest.getRequestURI());
            String contextPath = httpRequest.getContextPath();
            httpResponse.sendRedirect(contextPath + "/views/login.xhtml?faces-redirect=true&unauthorized=true");
        }
    }

    @Override
    public void destroy() {
        // Limpeza de recursos
    }
}
