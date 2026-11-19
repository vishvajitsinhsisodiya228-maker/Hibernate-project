package com.library;

public class CheckSetup {

    public static void main(String[] args) {
        // Building the SessionFactory loads the configuration and runs schema update.
        try {
            HibernateUtil.getSessionFactory();
            System.out.println("Hibernate setup successful. Check the Book table in library_db.");
        } finally {
            HibernateUtil.shutdown();
        }
    }
}
