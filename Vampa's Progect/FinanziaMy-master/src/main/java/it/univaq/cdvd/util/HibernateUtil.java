package it.univaq.cdvd.util;

import org.h2.tools.RunScript;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.io.FileReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Classe SINGLETON che si occupa di inizializzare la session factory di hibernate
 */
public class HibernateUtil {

    private static SessionFactory sessionFactory;

    private static String dbms = "/config/hibernate.cfg.xml";


    /**
     * Inizializza la session factory di hibernate
     */
    public static void initialize() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }

        try {
            System.out.println("Using dbms: " + dbms);

            sessionFactory = new Configuration()
                    .configure(dbms)
                    .buildSessionFactory();

            if ("/config/hibernate-test.cfg.xml".equals(dbms)) {
                // Inizializza il database H2 per i test
                Connection connection = DriverManager.getConnection("jdbc:h2:mem:testdb", "admin", "admin");
                RunScript.execute(connection, new StringReader("DROP ALL OBJECTS;"));
                RunScript.execute(connection, new FileReader("src/test/resources/finanziamy.sql"));
            }
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null || sessionFactory.isClosed()) {
            initialize();
        }
        return sessionFactory;
    }

    public static void setDbms(String dbms) {
        HibernateUtil.dbms = dbms;
    }

    public static void shutdown() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
