package com.library;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Scanner;

public class BookQueries {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter category: ");
            String category = scanner.nextLine().trim();

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                String hql = "from Book b where b.category = :cat order by b.price desc";
                Query<Book> query = session.createQuery(hql, Book.class);
                query.setParameter("cat", category);

                List<Book> books = query.getResultList();
                if (books.isEmpty()) {
                    System.out.println("No books found");
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
Test by entering:
Fiction
Science
A category that does not exist (for example, Unknown)
The final test should print: No books found
*/
