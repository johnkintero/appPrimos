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
 * |   1.0   |  28/09/2018  |      GAOQ      | Creacion de la clase que controla conexion a la base de datos de todo el aplicativo.                    |
 * |---------|--------------|----------------|---------------------------------------------------------------------------------------------------------|
 */
package com.primos.core.connectors;

import com.primos.core.logger.PR_Logger;
import java.io.File;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * This class control the connection spool, charges all resource and entities
 * registered into database.
 * @author Gustavo Adolfo Ovalle Quintero
 * @version 1.0
 * @since 29/09/2018
 */
public class ConnectionFactory{

    private static final PR_Logger LOG = PR_Logger.getLogger(ConnectionFactory.class);//Central LOG for this class.
    private static SessionFactory sessionFactory;//Actually database session.
    private static Configuration conf; //Actually session configuration.
    
    /**
     * Return the open session whit data base.
     * @return Session whit database.
     */
    public static Session getConnection() {
        try{
            configure();
            sessionFactory = conf.buildSessionFactory();
            return sessionFactory.openSession();
        }catch (Exception ex){
            LOG.fatal(ex);
            return null;
        }
    }
    
    /**
     * Restart pool connection whit database.
     * @return true if connection was restart. False otherwise.
     */
    public static boolean restartConnection() throws ClassNotFoundException{
        sessionFactory = null;
        configure();
        sessionFactory = conf.buildSessionFactory();
        return true;
    }
    
    /**
     * Close all database connections.
     * @return true if connection is close. False otherwise.
     */
    public static boolean closeConnection(){
        sessionFactory.close();
        return true;
    }
    
    /**
     * This method configure all parameters to establish the data base connection.
     */
    private static void configure() throws ClassNotFoundException{
        LOG.info("Iniciando conexión a la base de datos.");
        conf = new Configuration();
        LOG.info("se creo corretacmente el configurador de sesion, se procedera a configurar con el archivo: "+new File("/GABYVAL/conf/hibernate.xml").getAbsolutePath());
        conf.configure(new File("/PRIMOS/conf/hibernate.xml"));
        LOG.info("Se cargo la configuracion del archivo /PRIMOS/conf/hibernate.xml "+conf.toString());
    }
}