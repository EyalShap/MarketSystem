package com.sadna.sadnamarket;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

public class HibernateUtil {
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Dotenv dotenv = Dotenv.load();
            Configuration configuration = new Configuration().configure();
            configuration.setProperty("hibernate.connection.username", dotenv.get("DB_USERNAME"));
            configuration.setProperty("hibernate.connection.password", dotenv.get("DB_PASSWORD"));
            return configuration.buildSessionFactory();
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
