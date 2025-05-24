package com.conectapro.model.dao;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;

import com.conectapro.model.Cliente;
import com.conectapro.model.Prestador;
import com.conectapro.model.TipoUser;
import com.conectapro.model.Usuario;
import com.conectapro.util.jpa.Transactional;

public class CadastroUsuarioDao implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(CadastroUsuarioDao.class);
    
    @Inject
    private EntityManager em;
    
    @Transactional
    public void salvar(Usuario usuario) {
        logger.debug("Salvando usuário no banco de dados: " + usuario.getEmail());
        
        try {
            em.persist(usuario);
            em.flush(); // Forçar a execução do SQL para detectar erros imediatamente
            logger.info("Usuário salvo com sucesso. ID: " + usuario.getId());
        } catch (Exception e) {
            logger.error("Erro ao salvar usuário no banco de dados", e);
            throw new RuntimeException("Erro ao salvar usuário: " + e.getMessage(), e);
        }
    }
    
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
    
    public List<Usuario> listarTodos() {
        logger.debug("Listando todos os usuários");
        
        try {
            // Como Usuario é abstrato, precisamos buscar Cliente e Prestador separadamente
            TypedQuery<Cliente> queryCliente = em.createQuery(
                "SELECT c FROM Cliente c", Cliente.class);
            List<Cliente> clientes = queryCliente.getResultList();
            
            TypedQuery<Prestador> queryPrestador = em.createQuery(
                "SELECT p FROM Prestador p", Prestador.class);
            List<Prestador> prestadores = queryPrestador.getResultList();
            
            // Combinar os resultados
            List<Usuario> todos = new java.util.ArrayList<>(clientes);
            todos.addAll(prestadores);
            
            logger.debug("Total de usuários encontrados: " + todos.size());
            return todos;
        } catch (Exception e) {
            logger.error("Erro ao listar todos os usuários", e);
            throw new RuntimeException("Erro ao listar usuários: " + e.getMessage(), e);
        }
    }
    
    public Usuario buscarPorId(Long id) {
        logger.debug("Buscando usuário por ID: " + id);
        
        try {
            // Primeiro tentamos buscar como Cliente
            Cliente cliente = em.find(Cliente.class, id);
            if (cliente != null) {
                logger.debug("Usuário encontrado como Cliente: " + cliente.getId());
                return cliente;
            }
            
            // Se não encontrou como Cliente, tenta como Prestador
            Prestador prestador = em.find(Prestador.class, id);
            if (prestador != null) {
                logger.debug("Usuário encontrado como Prestador: " + prestador.getId());
                return prestador;
            }
            
            // Não encontrou nenhum
            logger.debug("Nenhum usuário encontrado com o ID: " + id);
            return null;
        } catch (Exception e) {
            logger.error("Erro ao buscar usuário por ID", e);
            throw new RuntimeException("Erro ao buscar usuário: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public void atualizar(Usuario usuario) {
        logger.debug("Atualizando usuário: " + usuario.getId());
        
        try {
            em.merge(usuario);
            em.flush(); // Forçar a execução do SQL para detectar erros imediatamente
            logger.info("Usuário atualizado com sucesso. ID: " + usuario.getId());
        } catch (Exception e) {
            logger.error("Erro ao atualizar usuário", e);
            throw new RuntimeException("Erro ao atualizar usuário: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public void excluir(Usuario usuario) {
        logger.debug("Excluindo usuário: " + usuario.getId());
        
        try {
            // Recarregar a entidade para garantir que está no contexto de persistência
            usuario = em.find(usuario.getClass(), usuario.getId());
            em.remove(usuario);
            em.flush(); // Forçar a execução do SQL para detectar erros imediatamente
            logger.info("Usuário excluído com sucesso. ID: " + usuario.getId());
        } catch (Exception e) {
            logger.error("Erro ao excluir usuário", e);
            throw new RuntimeException("Erro ao excluir usuário: " + e.getMessage(), e);
        }
    }
}
