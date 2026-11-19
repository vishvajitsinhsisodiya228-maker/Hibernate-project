package com.library;

import org.hibernate.Session;

public class FindBook {

    public static void main(String[] args) {
        // Pass a book ID as a program argument; default ID is 103.
        int id = (args.length > 0) ? Integer.parseInt(args[0]) : 103;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Book book = session.find(Book.class, id);

            if (book == null) {
                System.out.println("Book not found");
            } else {
                System.out.println("Book ID: " + book.getBookId());
                System.out.println("Title: " + book.getTitle());
                System.out.println("Author: " + book.getAuthor());
                System.out.printf("Price: ₹%.2f%n", book.getPrice());
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

/*
Test 1: Run with argument 103
Book ID: 103
Title: Rich Dad Poor Dad
Author: Robert Kiyosaki
Price: ₹499.00

Test 2: Run with argument 999
Book not found
*/
