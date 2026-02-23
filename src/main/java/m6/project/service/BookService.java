package m6.project.service;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;
import m6.project.model.Book;
import m6.project.model.Loan;
import m6.project.repository.BookRepository;
import m6.project.repository.LoanRepository;

public class BookService {
    private final BookRepository bookRepo;
    private final LoanRepository loanRepo;

    public BookService(EntityManager em) {
        this.bookRepo = new BookRepository(em);
        this.loanRepo = new LoanRepository(em);
    }

    public void displayAllBooks() {
        List<Book> books = bookRepo.findAll();
        if (books.isEmpty()) {
            System.out.println("No books found in the library.");
        } else {
            books.forEach(b ->
                System.out.println(b.getBookId() + " | " + b.getBookTitle() + " | " + b.getBookAuthor() + " | " + b.isAvailable())
            );
        }
    }

    public void displayAvailableBooks() {
        List<Book> books = bookRepo.findAvailableBooks();
        if (books.isEmpty()) {
            System.out.println("No available books.");
        } else {
            books.forEach(b ->
                System.out.println(b.getBookId() + " | " + b.getBookTitle() + " | " + b.getBookAuthor() + " | " + b.isAvailable())
            );
        }
    }

    public boolean hasAvailableBooks() {
        return !bookRepo.findAvailableBooks().isEmpty();
    }

    public boolean hasAnyBooks() {
        return !bookRepo.findAll().isEmpty();
    }

    public void addBook(Scanner input) {
        System.out.print("Enter ID: ");
        int id = input.nextInt();
        input.nextLine();

        System.out.print("Enter Title: ");
        String title = input.nextLine();

        System.out.print("Enter Author: ");
        String author = input.nextLine();

        Book book = new Book();
        book.setBookId(id);
        book.setBookTitle(title);
        book.setBookAuthor(author);
        book.setAvailable(true);

        bookRepo.save(book);
        System.out.println("Book added: " + book.getBookTitle());
    }

    public void removeBook(Scanner input) {
        System.out.print("Enter ID to remove: ");
        int id = input.nextInt();
        input.nextLine();

        Book book = bookRepo.findById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        // Check if book is borrowed or has loan record
        List<Loan> loans = loanRepo.findAll();
        boolean inLoan = loans.stream().anyMatch(l -> l.getLoanBookId() == id);

        if (!book.isAvailable() || inLoan) {
            System.out.println("Cannot remove book. It is currently borrowed or has an active loan.");
            return;
        }

        bookRepo.deleteById(id);
        System.out.println("Book removed. ID: " + id);
    }

    public void updateBook(Scanner input) {
        System.out.print("Enter ID to update: ");
        int id = input.nextInt();
        input.nextLine();

        Book book = bookRepo.findById(id);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        // Check if book is borrowed or has loan record
        List<Loan> loans = loanRepo.findAll();
        boolean inLoan = loans.stream().anyMatch(l -> l.getLoanBookId() == id);

        if (!book.isAvailable() || inLoan) {
            System.out.println("Cannot update book. It is currently borrowed or has an active loan.");
            return;
        }

        System.out.print("Enter new Title (leave blank to keep): ");
        String newTitle = input.nextLine();
        if (!newTitle.isBlank()) book.setBookTitle(newTitle);

        System.out.print("Enter new Author (leave blank to keep): ");
        String newAuthor = input.nextLine();
        if (!newAuthor.isBlank()) book.setBookAuthor(newAuthor);

        bookRepo.save(book);
        System.out.println("Book updated: " + book.getBookTitle());
    }
}
