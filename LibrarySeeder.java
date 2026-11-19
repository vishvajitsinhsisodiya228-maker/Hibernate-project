package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class LibrarySeeder {

    public static void main(String[] args) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Ten books, unique book codes BK-001 through BK-010.
            session.persist(new Book("The Silent Patient", "Alex Michaelides",
                    "BK-001", "Fiction", 450.00));
            session.persist(new Book("A Brief History of Time", "Stephen Hawking",
                    "BK-002", "Science", 550.00));
            session.persist(new Book("The Joy of Cooking", "Irma Rombauer",
                    "BK-003", "Cooking", 700.00));
            session.persist(new Book("The White Tiger", "Aravind Adiga",
                    "BK-004", "Fiction", 320.00));
            session.persist(new Book("Cosmos", "Carl Sagan",
                    "BK-005", "Science", 850.00));
            session.persist(new Book("Indian Cooking", " Madhur Jaffrey",
                    "BK-006", "Cooking", 600.00));
            session.persist(new Book("The Blue Umbrella", "Ruskin Bond",
                    "BK-007", "Fiction", 180.00));
            session.persist(new Book("The Selfish Gene", "Richard Dawkins",
                    "BK-008", "Science", 900.00));
            session.persist(new Book("Baking Basics", "Shivesh Bhatia",
                    "BK-009", "Cooking", 399.00));
            session.persist(new Book("Malgudi Days", "R. K. Narayan",
                    "BK-010", "Fiction", 120.00));

            transaction.commit();
            System.out.println("10 books inserted successfully.");
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
SELECT COUNT(*) FROM books;

Expected count after a fresh run: 10
Run this seeder only once. A second run will violate the unique book_code
constraint because BK-001 to BK-010 already exist.
*/
