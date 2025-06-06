package com.conectapro.util.jpa;

import java.io.Serializable;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnit;

import org.apache.log4j.Logger;

//import org.apache.log4j.Logger;

/**
 * @author murakamiadmin
 *
 */
@ApplicationScoped
public class EntityManagerProducer implements Serializable{

	private static final long serialVersionUID = 1L;
	private static final EntityManagerProducer INSTANCE = new EntityManagerProducer();
	
	@PersistenceUnit(unitName = "conectaproPU")
	private EntityManagerFactory factory;
	
	private Logger log = Logger.getLogger(EntityManagerProducer.class);

	
	public EntityManagerProducer() {
		log.debug("EntityManagerProducer: conectaproPU");
		this.factory = Persistence.createEntityManagerFactory("conectaproPU");
	}
	

	@Produces
	@RequestScoped
	public EntityManager create() {		
		log.info("Criou o EntityManager");
		return getFactory().createEntityManager();
		
	}

	public void close(@Disposes EntityManager manager) {		
		if (manager.isOpen()) {
			manager.close();
			log.info("Fechou o EntityManager");
		}		
	}
	
	public EntityManagerFactory getFactory() {
        return factory;
    }
	
	public static EntityManagerProducer getInstance() {
        return INSTANCE;
    }
	
}