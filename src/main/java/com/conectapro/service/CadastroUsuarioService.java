package com.conectapro.service;

import java.io.Serializable;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.conectapro.model.Cliente;
import com.conectapro.model.Prestador;
import com.conectapro.model.TipoUser;
import com.conectapro.model.Usuario;
import com.conectapro.model.dao.CadastroUsuarioDao;
import com.conectapro.util.jpa.Transactional;

public class CadastroUsuarioService implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final Logger logger = Logger.getLogger(CadastroUsuarioService.class);
    
    @Inject
    private CadastroUsuarioDao usuarioDao;
    
    @Transactional
    public void salvar(Usuario usuario) {
        logger.debug("Iniciando processo de cadastro de usuário: " + usuario.getEmail());
        
        // Verifica se já existe um usuário com o mesmo e-mail
        if (usuarioDao.buscarPorEmail(usuario.getEmail()) != null) {
            logger.warn("Tentativa de cadastro com e-mail já existente: " + usuario.getEmail());
            throw new RuntimeException("Já existe um usuário cadastrado com este e-mail.");
        }
        
        // Aplicar regras de negócio específicas para cada tipo de usuário
        if (usuario instanceof Cliente) {
            validarCliente((Cliente) usuario);
        } else if (usuario instanceof Prestador) {
            validarPrestador((Prestador) usuario);
        }
        
        // Criptografar a senha antes de salvar (em um ambiente real)
        // usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        // Salvar o usuário no banco de dados
        usuarioDao.salvar(usuario);
        
        logger.info("Usuário cadastrado com sucesso: " + usuario.getEmail());
    }
    
    private void validarCliente(Cliente cliente) {
        logger.debug("Validando dados de cliente: " + cliente.getEmail());
        
        // Validações específicas para cliente
        if (cliente.getCpf() != null && !cliente.getCpf().isEmpty()) {
            // Remover caracteres não numéricos para validação
            String cpfNumerico = cliente.getCpf().replaceAll("\\D", "");
            
            // Validar CPF (implementação simplificada)
            if (cpfNumerico.length() != 11) {
                throw new RuntimeException("CPF inválido.");
            }
            
            // Aqui você poderia implementar a validação completa do CPF
        }
    }
    
    private void validarPrestador(Prestador prestador) {
        logger.debug("Validando dados de prestador: " + prestador.getEmail());
        
        // Validações específicas para prestador
        if (prestador.getCnpj() != null && !prestador.getCnpj().isEmpty()) {
            // Remover caracteres não numéricos para validação
            String cnpjNumerico = prestador.getCnpj().replaceAll("\\D", "");
            
            // Validar CNPJ (implementação simplificada)
            if (cnpjNumerico.length() != 14) {
                throw new RuntimeException("CNPJ inválido.");
            }
            
            // Aqui você poderia implementar a validação completa do CNPJ
        }
        
        // Validar área de atuação
        if (prestador.getAreaAtuacao() == null || prestador.getAreaAtuacao().isEmpty()) {
            logger.warn("Prestador sem área de atuação definida: " + prestador.getEmail());
            // Você pode decidir se isso é um erro ou apenas um aviso
        }
    }
}
