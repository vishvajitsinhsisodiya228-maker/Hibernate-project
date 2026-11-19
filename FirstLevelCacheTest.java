package com.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class FirstLevelCacheTest {

    public static void main(String[] args) {
        int bookId = (args.length > 0) ? Integer.parseInt(args[0]) : 1;

        // Disable L2 cache only for this test so the second Session must issue SQL.
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .setProperty("hibernate.show_sql", "true")
                .setProperty("hibernate.cache.use_second_level_cache", "false")
                .buildSessionFactory();

        try {
            Book first;
            Book second;
            try (Session session1 = factory.openSession()) {
                first = session1.find(Book.class, bookId);
                second = session1.find(Book.class, bookId);

                System.out.println("Same object in first Session: " + (first == second));
            }

            try (Session session2 = factory.openSession()) {
                Book third = session2.find(Book.class, bookId);
                System.out.println("Same object across Sessions: " + (first == third));
            }
        } finally {
            factory.close();
        }
    }
}

/*
Expected:
Same object in first Session: true
Same object across Sessions: false

With a valid book ID and second-level cache disabled for this test,
Hibernate should print exactly 2 SELECT statements:
one in Session 1 and one in Session 2.
*/
