package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddBooks {

    public static void main(String[] args) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Updated for the new Book mapping. Generated IDs are not assigned.
            session.persist(new Book("The Alchemist", "Paulo Coelho",
                    "AB-101", "Fiction", 350.00));
            session.persist(new Book("Wings of Fire", "A. P. J. Abdul Kalam",
                    "AB-102", "Science", 280.00));
            session.persist(new Book("Rich Dad Poor Dad", "Robert Kiyosaki",
                    "AB-103", "Finance", 499.00));
            session.persist(new Book("The Guide", "R. K. Narayan",
                    "AB-104", "Fiction", 250.00));
            session.persist(new Book("Atomic Habits", "James Clear",
                    "AB-105", "Self-help", 650.00));
            session.persist(new Book("Think and Grow Rich", "Napoleon Hill",
                    "AB-106", "Finance", 799.00));

            transaction.commit();
            System.out.println("6 books inserted successfully.");
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

/*
Verify in MySQL:
SELECT * FROM books;

This updated version uses generated book IDs and unique book_code values.
Run only once, or choose unused book_code values before running again.
*/
