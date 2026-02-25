package m6.project.service;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import m6.project.model.Book;
import m6.project.model.Loan;
import m6.project.repository.BookRepository;
import m6.project.repository.LoanRepository;

public class BookService {
    private final BookRepository bookRepo;
    private final LoanRepository loanRepo;
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);

    public BookService(EntityManager em) {
        this.bookRepo = new BookRepository(em);
        this.loanRepo = new LoanRepository(em);
    }

    public void displayAllBooks() {
        List<Book> books = bookRepo.findAll();
        if (books.isEmpty()) {
            logger.info("No books found in the library.");
        } else {
            logger.info("ID | Title | Author | Availability");
            books.forEach(b ->
                logger.info("{} | {} | {} | {}", b.getBookId(), b.getBookTitle(), b.getBookAuthor(), b.isAvailable())
            );
        }
    }

    public void displayAvailableBooks() {
        List<Book> books = bookRepo.findAvailableBooks();
        if (books.isEmpty()) {
            logger.info("No available books.");
        } else {
            logger.info("ID | Title | Author | Availability");
            books.forEach(b ->
                logger.info("{} | {} | {} | {}", b.getBookId(), b.getBookTitle(), b.getBookAuthor(), b.isAvailable())
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
        try {
            logger.info("Enter ID: ");
            int id = input.nextInt();
            input.nextLine();

            logger.info("Enter Title: ");
            String title = input.nextLine();

            logger.info("Enter Author: ");
            String author = input.nextLine();

            Book book = new Book();
            book.setBookId(id);
            book.setBookTitle(title);
            book.setBookAuthor(author);
            book.setAvailable(true);

            bookRepo.save(book);
            logger.info("Added a book: {}", book.getBookTitle());
        } catch (Exception e) {
            logger.error("Error adding book", e);
        }
    }

    public void removeBook(Scanner input) {
        try {
            logger.info("Enter ID to remove: ");
            int id = input.nextInt();
            input.nextLine();

            Book book = bookRepo.findById(id);
            if (book == null) {
                logger.warn("Cannot remove: Book not found.");
                return;
            }

            // Delete any loan records tied to this book
            List<Loan> loans = loanRepo.findAll();
            loans.stream()
                 .filter(l -> l.getLoanBookId() == id)
                 .forEach(l -> {
                     loanRepo.deleteById(l.getLoanId());
                     logger.info("Deleted loan record for Book ID {}", id);
                 });

            bookRepo.deleteById(id);
            logger.info("Removed book. ID: {}", id);
        } catch (Exception e) {
            logger.error("Error removing book", e);
        }
    }

    public void updateBook(Scanner input) {
        try {
            logger.info("Enter ID to update: ");
            int id = input.nextInt();
            input.nextLine();

            Book book = bookRepo.findById(id);
            if (book == null) {
                logger.warn("Cannot update: Book not found.");
                return;
            }

            if (!book.isAvailable()) {
                logger.warn("Cannot update: Book is currently borrowed.");
                return;
            }

            logger.info("Enter new Title (leave blank to keep): ");
            String newTitle = input.nextLine();
            if (!newTitle.isBlank()) book.setBookTitle(newTitle);

            logger.info("Enter new Author (leave blank to keep): ");
            String newAuthor = input.nextLine();
            if (!newAuthor.isBlank()) book.setBookAuthor(newAuthor);

            bookRepo.save(book);
            logger.info("Updated book. ID: {} Title: {}", id, book.getBookTitle());
        } catch (Exception e) {
            logger.error("Error updating book", e);
        }
    }
}
