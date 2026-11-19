package com.library;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;

public class BookPager {

    private static final int PAGE_SIZE = 4;

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter page number: ");
            int page = scanner.nextInt();

            if (page < 1) {
                System.out.println("Please enter a page number starting from 1.");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<Book> query = session.createQuery(
                        "from Book b order by b.title", Book.class
                );

                int offset = (page - 1) * PAGE_SIZE;
                query.setFirstResult(offset);
                query.setMaxResults(PAGE_SIZE);

                List<Book> books = query.getResultList();
                if (books.isEmpty()) {
                    System.out.println("No more books");
                } else {
                    for (Book book : books) {
                        System.out.printf("%s - ₹%.2f%n",
                                book.getTitle(), book.getPrice());
                    }
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

/*
Test page 1, the last page, and a page number beyond the last page.
The beyond-last-page test prints: No more books
*/
