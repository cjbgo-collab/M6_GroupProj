package m6.project.service;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import m6.project.model.Book;
import m6.project.model.Loan;
import m6.project.model.User;
import m6.project.repository.BookRepository;
import m6.project.repository.LoanRepository;

public class LoanService {
    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;
    private static final Logger logger = LoggerFactory.getLogger(LoanService.class);

    public LoanService(EntityManager em) {
        this.loanRepo = new LoanRepository(em);
        this.bookRepo = new BookRepository(em);
    }

    // Display all borrowed books
    public void displayAllBorrowedBooks() {
        List<Loan> loans = loanRepo.findAll();
        if (loans.isEmpty()) {
            logger.info("No borrowed books found.");
        } else {
            logger.info("Loan ID | Title | Borrower | Book ID");
            loans.forEach(l ->
                logger.info("{} | {} | {} | {}", l.getLoanId(), l.getLoanBookTitle(), l.getBorrowerName(), l.getLoanBookId())
            );
        }
    }

    // Borrow a book
    public void borrowBook(Scanner input, User user) {
        try {
            logger.info("Enter Book ID to borrow: ");
            int bookId = input.nextInt();
            input.nextLine();

            Book book = bookRepo.findById(bookId);
            if (book == null) {
                logger.warn("Book not found. ID: {}", bookId);
                return;
            }
            if (!book.isAvailable()) {
                logger.warn("Book is already borrowed. ID: {}", bookId);
                return;
            }

            book.setAvailable(false);
            bookRepo.save(book);

            Loan loan = new Loan();
            loan.setLoanBookId(bookId);
            loan.setLoanBookTitle(book.getBookTitle());
            loan.setBorrowerName(user.getUserName());

            loanRepo.save(loan);
            logger.info("Borrowed book. Title: {} | Borrower: {} | Book ID: {}", book.getBookTitle(), user.getUserName(), bookId);
        } catch (Exception e) {
            logger.error("Error borrowing book", e);
        }
    }

    // Return a book
    public void returnBook(Scanner input) {
        try {
            logger.info("Enter Loan ID to return: ");
            int loanId = input.nextInt();
            input.nextLine();

            Loan loan = loanRepo.findById(loanId);
            if (loan == null) {
                logger.warn("Loan not found. ID: {}", loanId);
                return;
            }

            Book book = bookRepo.findById(loan.getLoanBookId());
            if (book != null) {
                book.setAvailable(true);
                bookRepo.save(book);
            }

            loanRepo.deleteById(loanId);
            logger.info("Returned book. Loan ID: {} | Book ID: {}", loanId, loan.getLoanBookId());
        } catch (Exception e) {
            logger.error("Error returning book", e);
        }
    }

    // Helper method for case 5 guard
    public boolean hasBorrowedBooks() {
        return !loanRepo.findAll().isEmpty();
    }
}
