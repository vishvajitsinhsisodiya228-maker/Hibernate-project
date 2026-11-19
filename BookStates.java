package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class BookStates {

    public static void main(String[] args) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Book book = new Book(
                    "Hibernate Made Easy",
                    "Vikram Joshi",
                    "ST-001",
                    "Science",
                    450.00
            );

            // Before persist(), the object is transient and has no generated ID.
            System.out.println("TRANSIENT id=" + book.getBookId()
                    + " inSession=" + session.contains(book));

            session.persist(book);

            // After persist(), the object is associated with this Session.
            System.out.println("PERSISTENT id=" + book.getBookId()
                    + " inSession=" + session.contains(book));

            // Dirty checking automatically saves this changed price at commit.
            book.setPrice(525.00);

            transaction.commit();
            System.out.println("Transaction committed. Updated price: ₹"
                    + book.getPrice());
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
Expected state output (generated ID may differ if rows already exist):
TRANSIENT id=null inSession=false
PERSISTENT id=<generated id> inSession=true

Hibernate assigns the ID during persist() for IDENTITY generation.
The changed price is stored automatically at transaction commit by
Hibernate dirty checking; no second persist/save/update call is required.
*/
