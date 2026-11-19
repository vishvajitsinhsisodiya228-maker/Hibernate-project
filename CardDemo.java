package com.library;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class CardDemo {

    public static void main(String[] args) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Member member = new Member("Neha Desai", "Surat", 2026);
            LibraryCard card = new LibraryCard("LC-001", 2030);

            // Set the relationship and persist only the Member.
            member.setLibraryCard(card);
            session.persist(member);

            transaction.commit();
            System.out.println("Member and library card saved successfully.");
            System.out.println("Generated member ID: " + member.getMemberId());
            System.out.println("Generated card ID: " + card.getCardId());
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
CardDemo uses CascadeType.ALL, so persisting Member also persists LibraryCard.
Run once with card number LC-001 because card_number is unique.
*/
