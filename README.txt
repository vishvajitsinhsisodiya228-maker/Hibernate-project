LIBRARY HIBERNATE - RELATIONSHIP PRACTICALS
===========================================

Requirements:
- JDK 17 or later
- Maven
- MySQL Server

DATABASE SETUP
1. Start MySQL.
2. Set your MySQL username/password in src/main/resources/hibernate.cfg.xml.
3. Keep hibernate.hbm2ddl.auto=update.
4. Run CheckSetup.java once after adding the entity mappings. Hibernate creates
   or updates library_cards, loans, and member_wishlist from the annotations.
5. Existing Book/Member records are not automatically reset.

BUILD
    mvn clean compile

TASK 1 - ONE-TO-ONE MEMBER AND LIBRARY CARD
Run:
    mvn exec:java -Dexec.mainClass=com.library.CardDemo

CardDemo creates a Member and LibraryCard and calls persist() only on Member.
CascadeType.ALL saves the card automatically. LC-001 is unique, so run once
or change the card number before another run.

TASK 2 - MANY-TO-ONE LOANS
Open LoanDemo.java and confirm the IDs at the top:
    memberId = 1
    bookId1 = 1, bookId2 = 2, bookId3 = 3

Change them if your existing records have different generated IDs.
Run:
    mvn exec:java -Dexec.mainClass=com.library.LoanDemo

It creates three loans, each with daysKept=5 and returned=false.
Run once to avoid adding duplicate demo loans.

TASK 3 - MANY-TO-MANY WISHLISTS
Open WishlistDemo.java and confirm member IDs 1 and 2 and book IDs 1 to 5.
Change them to IDs that exist in your database.
Run:
    mvn exec:java -Dexec.mainClass=com.library.WishlistDemo

Verify:
    SELECT * FROM member_wishlist;

For initially empty wishlists, the join table should contain five rows.
Run once; repeated member/book pairs may violate the join table primary key.

TASK 4 - LOAN REPORT WITH LAZY LOADING
Test member 1 (expected to have the three loans created above):
    mvn exec:java -Dexec.mainClass=com.library.LoanReport -Dexec.args="1"

Test a member with no loans, for example member 2:
    mvn exec:java -Dexec.mainClass=com.library.LoanReport -Dexec.args="2"

The program accesses member.getLoans() while the Session is open. The loans
mapping remains FetchType.LAZY; it is not changed to EAGER.

ENTITY MAPPINGS
- Member -> LibraryCard: @OneToOne, CascadeType.ALL, LAZY
- Member -> Loan: @OneToMany(mappedBy="member"), LAZY
- Loan -> Member: @ManyToOne, LAZY, owns member_id
- Loan -> Book: @ManyToOne, LAZY, owns book_id
- Member -> Book wishlist: @ManyToMany through member_wishlist

IMPORTANT
- CheckSetup registers Book, Member, LibraryCard, and Loan in hibernate.cfg.xml.
- No manual CREATE TABLE statements are included.
- Use generated IDs from your actual database; do not assume they are always
  1, 2, 3 if you have already inserted earlier test data.
- Hibernate 6 uses jakarta.persistence annotations.


============================================================
NEW PRACTICALS: CRUD, HQL, PAGINATION, CRITERIA AND CACHING
============================================================

MAVEN BUILD
    mvn clean compile

TASK A1 - BOOK CRUD
    mvn exec:java -Dexec.mainClass=com.library.BookCrudDemo

Creates Test Book for ₹300, reads it, increases the price to ₹330,
deletes it using Session.remove(), then reads again and prints Deleted.
The unique book_code CRUD-01 must not already exist.

TASK A2 - HQL CATEGORY QUERY
    mvn exec:java -Dexec.mainClass=com.library.BookQueries

Enter Fiction, Science, or a category with no books. Uses HQL named
parameter :cat and setParameter(), sorted by price descending.

TASK A3 - PAGINATION
    mvn exec:java -Dexec.mainClass=com.library.BookPager

Enter page 1, the last page, or a page beyond the last. Four books per page;
beyond the last page prints No more books.

TASK A4 - CRITERIA API SEARCH
    mvn exec:java -Dexec.mainClass=com.library.BookSearch

Enter 200 then 500; also test 5000 then 6000. Uses JPA CriteriaBuilder.

TASK B1 - FIRST-LEVEL CACHE
    mvn exec:java -Dexec.mainClass=com.library.FirstLevelCacheTest -Dexec.args="1"

Uses a separate SessionFactory with second-level cache disabled for this test.
Within one Session, repeated find() returns the same Java object. A second
Session loads a different object. For an existing ID, expect 2 SELECTs.

TASK B2 - SECOND-LEVEL CACHE
    mvn exec:java -Dexec.mainClass=com.library.SecondLevelCacheTest -Dexec.args="1"

Uses Hibernate JCache integration with Ehcache 3. On a cold cache and a valid
book ID, expect one SELECT across the two Sessions. If the cache was already
warm, it may issue zero SELECTs. Cache configuration is in hibernate.cfg.xml
and ehcache.xml. Book is annotated with @Cacheable and READ_WRITE.

TASK B3 - CACHE PERFORMANCE
Run cache ON:
    mvn exec:java -Dexec.mainClass=com.library.CachePerformance -Dexec.args="on"

Run cache OFF:
    mvn exec:java -Dexec.mainClass=com.library.CachePerformance -Dexec.args="off"

The program gets all book IDs before timing, then performs 50 rounds where
each book is loaded in a new Session. It prints total load time and L2 hits.
Paste your actual two outputs into the comment at the end of CachePerformance.java.

TASK B4 - LOAN SUMMARY
    mvn exec:java -Dexec.mainClass=com.library.LoanSummary

Uses one HQL query:
select distinct m from Member m left join fetch m.loans l left join fetch l.book

Expected prepared statement count: 1.

IMPORTANT
- Use valid IDs from your own database when running cache tests.
- Ensure the database contains Book, Member and Loan records for the
  relevant practicals.
- Cache hit/select counts depend on whether the cache is already warm.
- Hibernate statistics are enabled in hibernate.cfg.xml.
- This project uses plain Hibernate 6; it does not use Spring Boot.
