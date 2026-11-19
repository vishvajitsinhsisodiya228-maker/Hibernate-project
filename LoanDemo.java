package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class LoanDemo {

    public static void main(String[] args) {
        // Change these IDs if your existing member/books use different IDs.
        int memberId = 1;
        int bookId1 = 1;
        int bookId2 = 2;
        int bookId3 = 3;

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Member member = session.find(Member.class, memberId);
            Book book1 = session.find(Book.class, bookId1);
            Book book2 = session.find(Book.class, bookId2);
            Book book3 = session.find(Book.class, bookId3);

            if (member == null || book1 == null || book2 == null || book3 == null) {
                throw new IllegalStateException(
                    "Member or one of the books was not found. Check the IDs at the top of LoanDemo."
                );
            }

            // Loan owns the member_id and book_id foreign keys.
            Loan loan1 = new Loan(5, false, member, book1);
            Loan loan2 = new Loan(5, false, member, book2);
            Loan loan3 = new Loan(5, false, member, book3);

            loan1.setMember(member);
            loan2.setMember(member);
            loan3.setMember(member);

            session.persist(loan1);
            session.persist(loan2);
            session.persist(loan3);

            transaction.commit();
            System.out.println("3 loans inserted successfully for " + member.getFullName());
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
Before running, ensure members has member_id=1 and books has book_id=1,2,3.
Change the IDs above to match your database. Run only once to avoid extra loans.
*/
