package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class AddMembers {

    public static void main(String[] args) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            // Persist five library members from different Indian cities.
            session.persist(new Member("Aarav Patel", "Ahmedabad", 2024));
            session.persist(new Member("Priya Sharma", "Jaipur", 2023));
            session.persist(new Member("Rohan Mehta", "Mumbai", 2025));
            session.persist(new Member("Ananya Iyer", "Chennai", 2022));
            session.persist(new Member("Kabir Singh", "Delhi", 2024));

            transaction.commit();
            System.out.println("5 members inserted successfully.");
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
Verify in MySQL:
SELECT * FROM members;

For a fresh members table, generated member_id values should be 1 to 5.
Run this program only once to avoid inserting duplicate test members.
*/
