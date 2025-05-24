package com.conectapro.model.dao;

import java.io.Serializable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.conectapro.model.Cliente;
import com.conectapro.model.Prestador;
import com.conectapro.model.Usuario;

public class LoginDao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(LoginDao.class);
    
    @Inject
    private EntityManager em;
    
    /**
     * Busca um usuário pelo email.
     * 
     * @param email O email do usuário a ser buscado
     * @return O objeto Usuario se encontrado, ou null caso contrário
     */
    public Usuario buscarPorEmail(String email) {
        logger.debug("Buscando usuário por e-mail: " + email);
        
        try {
            // Primeiro tentamos buscar como Cliente
            TypedQuery<Cliente> queryCliente = em.createQuery(
                "SELECT c FROM Cliente c WHERE c.email = :email", Cliente.class);
            queryCliente.setParameter("email", email);
            
            try {
                Cliente cliente = queryCliente.getSingleResult();
                logger.debug("Usuário encontrado como Cliente: " + cliente.getId());
                return cliente;
            } catch (NoResultException e) {
                // Não encontrou como Cliente, tenta como Prestador
                TypedQuery<Prestador> queryPrestador = em.createQuery(
                    "SELECT p FROM Prestador p WHERE p.email = :email", Prestador.class);
                queryPrestador.setParameter("email", email);
                
                try {
                    Prestador prestador = queryPrestador.getSingleResult();
                    logger.debug("Usuário encontrado como Prestador: " + prestador.getId());
                    return prestador;
                } catch (NoResultException e2) {
                    // Não encontrou nenhum
                    logger.debug("Nenhum usuário encontrado com o e-mail: " + email);
                    return null;
                }
            }
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por e-mail", e);
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }
    }
}
