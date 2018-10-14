/* *****************************************************************************************************************************************************
 * *****************************************************************************************************************************************************
 * ***************************** ************ ********** ************ ******     ****** **************** **************** ******************************
 * ***************************** ************ ********** ************  ******   ******  **************** **************** ******************************
 * ***************************** **      **** **    ****      **       ******* ***  **  ****        **** ****             ******************************
 * ***************************** **      **** **    ****      **       **   ******  **  ****        **** ****             ******************************
 * ***************************** **      **** **    ****      **       **   **  **  **  ****        **** ****             ******************************
 * ***************************** ************ **********      **       **   **  **  **  ****        **** **************** ******************************
 * ***************************** ************ **********      **       **   **  **  **  ****        **** **************** ******************************
 * ***************************** **           ****            **       **   **  **  **  ****        ****             **** ******************************
 * ***************************** **           ** **           **       **   **  **  **  ****        ****             **** ******************************
 * ***************************** **           **  **          **       **   **  **  **  ****        ****             **** ******************************
 * ***************************** **           **   **         **       **   **  **  **  ****        ****             **** ******************************
 * ***************************** **           **    **   ************  **   **  **  **  **************** **************** ******************************
 * ***************************** **           **     **  ************  **   **  **  **  **************** **************** ******************************
 * *****************************************************************************************************************************************************
 * *****************************************************************************************************************************************************
 * *****************************************************************************************************************************************************
 * |---------------------------------------------------------------------------------------------------------------------------------------------------|
 * |                                                        Control de versiones                                                                       |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 * | Version |    Fecha     |  Responsable   |                                                  Comentarios                                            |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 * |   1.0   |  14/10/2018  |      GAOQ      | Creacion de la clase las transacciones de persistencia de la aplicacion.                                |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 */
package com.primos.core.persistence;

import com.primos.core.connectors.ConnectionFactory;
import com.primos.core.logger.PR_Logger;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * This class will control the persistence with database.
 * @author GAOQ
 * @version 1.0
 * @since 14/10/2018
 */
public class PersistenceManager implements EntityManager{
    
    private final PR_Logger LOG = PR_Logger.getLogger(PersistenceManager.class);//Central log for this class.
    private final Session SESSION;
    private static PersistenceManager instance;//Instance of this class.
    private final Lock lock = new ReentrantLock();
    
    public PersistenceManager() {
        System.out.println("Iniciando...");
        SESSION = ConnectionFactory.getConnection();//Connection with database.
    }
    
    /**
     * This method return the instance of this controller.
     * @return PersistenceManager this instance.
     */
    public static PersistenceManager getInstance() {
        if (instance == null){
            instance = new PersistenceManager();
        }
        return instance;
    }
    
    public void closeConnection(){
        SESSION.close();
    }
    
    /**
     * Charge any object from database, if the entity is mapped.
     * @param classType Class of the entity mapped.
     * @param id Serializable the unique ID for the object.
     * @return Object the object that was recovery.
     */
    @Override
    public Object load(Class classType, Serializable id){
        LOG.debug("The system has gone to load the object... Entity to recovery "+classType.getCanonicalName());
        try{
            Object o = SESSION.get(classType, id);
            if(o != null){
                return o;
            }else{
                LOG.error("");
                return null;
            }
        }catch(Exception ex){
            LOG.error(ex);
            throw ex;
        }
    }

    /**
     * This method allow save any object into database.
     * @param o Object the object to save.
     * @return boolean true if the object can be save, false otherwise.
     */
    @Override
    public boolean save(Object o) {
        Transaction tx = startTransaccion();
        LOG.debug("The transaction is started for save in database.");
        if(tx != null){
            try{
                SESSION.save(o);
                LOG.debug("The object was save in database.");
                tx.commit();
                LOG.debug("The transactions finished successfully.");
                lock.unlock();
                return true;
            } catch (Exception ex){
                lock.unlock();
                LOG.error(ex);
                tx.rollback();
                SESSION.clear();
                throw ex;
            }
        }else{
           LOG.error("");
           return false;
        }
    }

    /**
     * This method allow update the object.
     * @param o Object the object to update.
     * @return boolean true if the object was update successfully, false otherwise.
     */
    @Override
    public boolean update(Object o){
       Transaction tx = startTransaccion();
       LOG.debug("The transaction is started for update in database. "+o);
       if(tx != null){
           if(o != null){
               try{
                   SESSION.update(o);
                   LOG.debug("The object was update in database.");
                   tx.commit();
                   LOG.debug("The transactions finished successfully.");
                   lock.unlock();
                   return true;
                } catch (Exception ex){
                   lock.unlock();
                   LOG.error(ex);
                   tx.rollback();
                   throw ex;
                }
           }else{
               LOG.error("");
               return false;
           }
        }else{
            LOG.error("");
            return false;
        }
    }

    /**
     * Obtain all objects of any kind.
     * @param classType Class class to load.
     * @return List of objects from database.
     */
    @Override
    public synchronized List getAll(Class classType){
        try{
            Query q = SESSION.createQuery("FROM "+classType.getSimpleName());
            return q.list();
        }catch(Exception ex){
            LOG.error(ex);
            return null;
        }
    }
    
    /**
     * This method execute a SQL sentence.
     * @param sql String the SQL sentence.
     * @return List return all objects found for the sentence.
     */
    @Override
    public List runSQL(String sql) {
        try{
            SQLQuery q = SESSION.createSQLQuery(sql);
            return q.list();
        }catch(Exception ex){
            LOG.error(ex);
            return null;
        }
    }
    
    /**
     * This method execute a criteria HQL, the from expression must contain a entity name mapped.
     * @param sql String the criteria.
     * @return List of objects.
     */
    public List runCriteria(String sql) {
        try{
            Query q = SESSION.createQuery(sql);
            return q.list();
        }catch(Exception ex){
            LOG.error(ex);
            return null;
        }
    }
    
    
    public Transaction startTransaccion() {
        if(lock.tryLock()){
            return SESSION.beginTransaction();
        }
        return null;
    }
    
    /**
     * This method refresh the instance of a entity whit database info.
     * @param o Object to update.
     */
    public void refresh(Object o) {
        try {
            LOG.debug("The object is reload from data base.");
            SESSION.refresh(o);
            LOG.debug("Reload is complete.");
        } catch (Exception ex) {
            LOG.fatal(ex);
        }
        
    }
    
    public void delete(Object o) {
        Transaction tx = startTransaccion();
        if(tx != null){
            try{
                SESSION.delete(o);
                tx.commit();
                lock.unlock();
            } catch (Exception ex) {
                lock.unlock();
                LOG.error("");
                LOG.error(ex);
                tx.rollback();
                LOG.error("");
            }
        }else{
           LOG.error("");
        }
    }
    
    public boolean isOpen(){
        if(SESSION == null){
            return false;
        }
        return SESSION.isOpen();
    }
    
    public void closeSesion(){
        SESSION.clear();
        SESSION.close();
        instance = null;
    }
}
