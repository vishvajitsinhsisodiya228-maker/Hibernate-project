package com.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.stat.Statistics;

import java.util.List;

public class CachePerformance {

    public static void main(String[] args) {
        boolean cacheEnabled = args.length == 0 || args[0].equalsIgnoreCase("on");

        // Build the SessionFactory and retrieve IDs before starting the timer.
        Configuration configuration = new Configuration()
                .configure("hibernate.cfg.xml")
                .setProperty("hibernate.generate_statistics", "true")
                .setProperty("hibernate.cache.use_second_level_cache",
                        Boolean.toString(cacheEnabled));

        SessionFactory factory = configuration.buildSessionFactory();

        try {
            List<Integer> bookIds;
            try (Session session = factory.openSession()) {
                bookIds = session.createQuery(
                        "select b.bookId from Book b order by b.bookId", Integer.class
                ).getResultList();
            }

            Statistics statistics = factory.getStatistics();
            statistics.clear();

            long start = System.nanoTime();

            // Each lookup is performed in a fresh Session; repeat for 50 rounds.
            for (int round = 0; round < 50; round++) {
                for (Integer id : bookIds) {
                    try (Session session = factory.openSession()) {
                        session.find(Book.class, id);
                    }
                }
            }

            long elapsedNanos = System.nanoTime() - start;
            double elapsedMillis = elapsedNanos / 1_000_000.0;

            System.out.println("Second-level cache: " + (cacheEnabled ? "ON" : "OFF"));
            System.out.printf("Total loading time: %.3f ms%n", elapsedMillis);
            System.out.println("Second-level cache hit count: "
                    + statistics.getSecondLevelCacheHitCount());
            System.out.println("Book IDs loaded per round: " + bookIds.size());
            System.out.println("Rounds: 50");
        } finally {
            factory.close();
        }
    }
}

/*
Run separately:
mvn exec:java -Dexec.mainClass=com.library.CachePerformance -Dexec.args="on"
mvn exec:java -Dexec.mainClass=com.library.CachePerformance -Dexec.args="off"

Paste the actual output from your computer here after both runs.
Results depend on machine speed, database size and cache warm-up.
*/
