package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class WishlistDemo {

    public static void main(String[] args) {
        // Change IDs if your existing data uses different generated IDs.
        int firstMemberId = 1;
        int secondMemberId = 2;

        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Member firstMember = session.find(Member.class, firstMemberId);
            Member secondMember = session.find(Member.class, secondMemberId);

            // Load all five books inside the same transaction.
            Book book1 = session.find(Book.class, 1);
            Book book2 = session.find(Book.class, 2);
            Book book3 = session.find(Book.class, 3);
            Book book4 = session.find(Book.class, 4);
            Book book5 = session.find(Book.class, 5);

            if (firstMember == null || secondMember == null
                    || book1 == null || book2 == null || book3 == null
                    || book4 == null || book5 == null) {
                throw new IllegalStateException(
                    "Required member/book not found. Update the IDs to match your database."
                );
            }

            firstMember.getWishlist().add(book1);
            firstMember.getWishlist().add(book2);
            firstMember.getWishlist().add(book3);

            secondMember.getWishlist().add(book4);
            secondMember.getWishlist().add(book5);

            transaction.commit();
            System.out.println("Wishlist saved: 3 books for first member and 2 for second member.");
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
Verify:
SELECT * FROM member_wishlist;

For empty initial wishlists, the join table should contain 5 rows.
Run only once to avoid duplicate member/book pairs.
*/
