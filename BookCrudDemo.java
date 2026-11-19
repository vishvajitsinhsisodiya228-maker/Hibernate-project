package com.library;

public class BookCrudDemo {

    public static void main(String[] args) {
        BookCrud crud = new BookCrud();

        try {
            // Create a test book with an unused unique code.
            Book newBook = new Book(
                    "Test Book", "Library Practical", "CRUD-01", "Fiction", 300.00
            );
            Integer id = crud.createBook(newBook);
            System.out.println("Created book ID: " + id);

            // Read the newly created book.
            Book found = crud.readBook(id);
            if (found != null) {
                System.out.printf("Read: %s | ₹%.2f%n",
                        found.getTitle(), found.getPrice());
            }

            // Increase price by 10 percent.
            crud.raisePrice(id, 10.0);
            Book updated = crud.readBook(id);
            if (updated != null) {
                System.out.printf("After 10%% increase: ₹%.2f%n", updated.getPrice());
            }

            // Delete, then read again.
            crud.deleteBook(id);
            Book afterDelete = crud.readBook(id);
            if (afterDelete == null) {
                System.out.println("Deleted");
            } else {
                System.out.println("Delete failed");
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }
}

/*
Expected flow:
Created book ID: <generated ID>
Read: Test Book | ₹300.00
After 10% increase: ₹330.00
Deleted

Use a different book_code if CRUD-01 already exists.
*/
