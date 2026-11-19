package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class BookCrud {

    // CREATE: each operation uses its own Session and Transaction.
    public Integer createBook(Book book) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(book);
            tx.commit();
            return book.getBookId();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // READ: returns a detached Book after this method closes its Session.
    public Book readBook(Integer bookId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Book book = session.find(Book.class, bookId);
            tx.commit();
            return book;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // UPDATE: dirty checking writes the changed price when the transaction commits.
    public void raisePrice(Integer bookId, double percentage) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Book book = session.find(Book.class, bookId);
            if (book == null) {
                throw new IllegalArgumentException("Book not found: " + bookId);
            }
            book.setPrice(book.getPrice() * (1.0 + percentage / 100.0));
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }

    // DELETE: uses Session.remove(), not HQL.
    public void deleteBook(Integer bookId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Book book = session.find(Book.class, bookId);
            if (book != null) {
                session.remove(book);
            }
            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) tx.rollback();
            throw e;
        }
    }
}
