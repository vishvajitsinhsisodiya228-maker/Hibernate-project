package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class LoanReport {

    public static void main(String[] args) {
        // Supply a member ID as an argument; default is member 1.
        int memberId = (args.length > 0) ? Integer.parseInt(args[0]) : 1;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Keep the Session open while accessing the lazy loans collection.
            Member member = session.find(Member.class, memberId);

            if (member == null) {
                System.out.println("Member not found");
                return;
            }

            System.out.println("Member: " + member.getFullName());

            // Accessing loans here initializes the LAZY collection within the Session.
            if (member.getLoans().isEmpty()) {
                System.out.println("No loans");
            } else {
                System.out.println("Number of loans: " + member.getLoans().size());

                for (Loan loan : member.getLoans()) {
                    Book book = loan.getBook();
                    System.out.println("Borrowed book: "
                            + (book != null ? book.getTitle() : "Book not found"));
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

/*
Test a member with three loans:
mvn exec:java -Dexec.mainClass=com.library.LoanReport -Dexec.args="1"

Test a member with no loans:
mvn exec:java -Dexec.mainClass=com.library.LoanReport -Dexec.args="2"

Use IDs that match your own database. The loans collection remains LAZY;
it is read before the Session is closed to avoid LazyInitializationException.
*/
