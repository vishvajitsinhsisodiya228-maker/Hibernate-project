package com.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;

public class SecondLevelCacheTest {

    public static void main(String[] args) {
        int bookId = (args.length > 0) ? Integer.parseInt(args[0]) : 1;

        SessionFactory factory = HibernateUtil.getSessionFactory();
        Statistics statistics = factory.getStatistics();
        statistics.clear();

        try {
            try (Session session1 = factory.openSession()) {
                Book book = session1.find(Book.class, bookId);
                System.out.println("Session 1: "
                        + (book != null ? book.getTitle() : "Book not found"));
            }

            try (Session session2 = factory.openSession()) {
                Book book = session2.find(Book.class, bookId);
                System.out.println("Session 2: "
                        + (book != null ? book.getTitle() : "Book not found"));
            }

            System.out.println("Second-level cache hits: "
                    + statistics.getSecondLevelCacheHitCount());
            System.out.println("Second-level cache puts: "
                    + statistics.getSecondLevelCachePutCount());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

/*
Expected on a cold cache, for a valid ID:
- The first Session issues one SELECT and puts Book in L2 cache.
- The second Session gets Book from L2 cache without another SELECT.
So the console should show exactly 1 SELECT statement.
If the cache was already warm before the test, it may show 0 SELECTs.
*/
