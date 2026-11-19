package com.library;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Integer memberId;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "city")
    private String city;

    @Column(name = "joined_year")
    private int joinedYear;

    // Member owns the one-to-one relationship. Persisting Member also saves its card.
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private LibraryCard libraryCard;

    // Loan owns the foreign key; this is the inverse side.
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    private List<Loan> loans = new ArrayList<>();

    // Member owns the wishlist join table.
    @ManyToMany
    @JoinTable(
        name = "member_wishlist",
        joinColumns = @JoinColumn(name = "member_id"),
        inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> wishlist = new HashSet<>();

    public Member() {
    }

    public Member(String fullName, String city, int joinedYear) {
        this.fullName = fullName;
        this.city = city;
        this.joinedYear = joinedYear;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getJoinedYear() {
        return joinedYear;
    }

    public void setJoinedYear(int joinedYear) {
        this.joinedYear = joinedYear;
    }

    public LibraryCard getLibraryCard() {
        return libraryCard;
    }

    public void setLibraryCard(LibraryCard libraryCard) {
        this.libraryCard = libraryCard;
    }

    public List<Loan> getLoans() {
        return loans;
    }

    public Set<Book> getWishlist() {
        return wishlist;
    }
}
