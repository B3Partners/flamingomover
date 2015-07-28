/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.b3p.flamingomover;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Meine Toonen <meinetoonen@b3partners.nl>
 */
public class EMFListener implements ServletContextListener{
    private static final Log log = LogFactory.getLog(EMFListener.class);
    private static EntityManagerFactory oracle;
    private static EntityManagerFactory postgres;

    public static EntityManager getPostgresEM(){
        return postgres.createEntityManager();
    }

    public static EntityManager getOracleEM(){
        return oracle.createEntityManager();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Map<Object, Object> propsSrc= new HashMap<Object,Object>();
            propsSrc.put("javax.persistence.nonJtaDataSource", "java:comp/env/jdbc/oracle");

            oracle = Persistence.createEntityManagerFactory("viewer-config-oracle", propsSrc);

            Map<Object, Object> propsDest= new HashMap<Object,Object>();
            propsDest.put("javax.persistence.nonJtaDataSource", "java:comp/env/jdbc/postgres");
            postgres = Persistence.createEntityManagerFactory("viewer-config-postgresql", propsDest);
        } catch (Exception e) {
            log.error("cannot instantiate emfs: ",e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if(oracle!= null && oracle.isOpen()){
            oracle.close();
        }
        if(postgres!= null && postgres.isOpen()){
            postgres.close();
        }
    }

}
