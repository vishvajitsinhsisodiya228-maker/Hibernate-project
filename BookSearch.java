package com.library;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.List;
import java.util.Scanner;

public class BookSearch {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.print("Enter minimum price: ");
            double minPrice = scanner.nextDouble();

            System.out.print("Enter maximum price: ");
            double maxPrice = scanner.nextDouble();

            if (minPrice > maxPrice) {
                System.out.println("Minimum price cannot exceed maximum price.");
                return;
            }

            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                CriteriaBuilder cb = session.getCriteriaBuilder();
                CriteriaQuery<Book> cq = cb.createQuery(Book.class);
                Root<Book> root = cq.from(Book.class);

                Predicate fiction = cb.equal(root.get("category"), "Fiction");
                Predicate priceRange = cb.between(root.get("price"), minPrice, maxPrice);
                cq.select(root).where(cb.and(fiction, priceRange))
                        .orderBy(cb.asc(root.get("price")));

                List<Book> books = session.createQuery(cq).getResultList();
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
Test ranges:
200 to 500
5000 to 6000 (expected: No books found)
Uses JPA CriteriaBuilder, not the old org.hibernate.Criteria API.
*/
