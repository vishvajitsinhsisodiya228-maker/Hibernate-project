package com.library;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.hibernate.stat.Statistics;

import java.util.List;

public class LoanSummary {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        Statistics statistics = factory.getStatistics();

        try (Session session = factory.openSession()) {
            // Clear counters immediately before the single fetch-join query.
            statistics.clear();

            String hql = "select distinct m from Member m "
                    + "left join fetch m.loans l "
                    + "left join fetch l.book";

            Query<Member> query = session.createQuery(hql, Member.class);
            List<Member> members = query.getResultList();

            for (Member member : members) {
                System.out.println("Member: " + member.getFullName());

                if (member.getLoans().isEmpty()) {
                    System.out.println("  No loans");
                } else {
                    for (Loan loan : member.getLoans()) {
                        Book book = loan.getBook();
                        System.out.println("  " + (book != null
                                ? book.getTitle() : "Book not found"));
                    }
                }
            }

            System.out.println("Prepared statement count: "
                    + statistics.getPrepareStatementCount());
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

/*
Expected prepared statement count: 1
The left fetch joins load members, loans and each loan's book in one query.
*/
