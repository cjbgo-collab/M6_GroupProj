package m6.project.service;

import jakarta.persistence.EntityManager;
import java.util.List;
import java.util.Scanner;

import m6.project.model.Book;
import m6.project.model.Loan;
import m6.project.model.User;
import m6.project.repository.BookRepository;
import m6.project.repository.LoanRepository;

public class LoanService {
    private final LoanRepository loanRepo;
    private final BookRepository bookRepo;

    public LoanService(EntityManager em) {
        this.loanRepo = new LoanRepository(em);
        this.bookRepo = new BookRepository(em);
    }

    
    public void displayAllBorrowedBooks() {
        List<Loan> loans = loanRepo.findAll();
        if (loans.isEmpty()) {
            System.out.println("No borrowed books found.");
        } else {
            loans.forEach(l ->
                System.out.println(l.getLoanId() + " | " + l.getLoanBookTitle() + " | " + l.getBorrowerName())
            );
        }
    }

    
    public void borrowBook(Scanner input, User user) {
        System.out.print("Enter Book ID to borrow: ");
        int bookId = input.nextInt();
        input.nextLine();

        Book book = bookRepo.findById(bookId);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        if (!book.isAvailable()) {
            System.out.println("Book is already borrowed.");
            return;
        }

        
        book.setAvailable(false);
        bookRepo.save(book);

        
        Loan loan = new Loan();
        loan.setLoanBookId(bookId);
        loan.setLoanBookTitle(book.getBookTitle());
        loan.setBorrowerName(user.getUserName());

        loanRepo.save(loan);
        System.out.println("Book borrowed: " + loan.getLoanBookTitle() + " by " + loan.getBorrowerName());
    }

   
    public void returnBook(Scanner input) {
        System.out.print("Enter Loan ID to return: ");
        int loanId = input.nextInt();
        input.nextLine();

        Loan loan = loanRepo.findById(loanId);
        if (loan == null) {
            System.out.println("Loan not found.");
            return;
        }

        
        Book book = bookRepo.findById(loan.getLoanBookId());
        if (book != null) {
            book.setAvailable(true);
            bookRepo.save(book);
        }

     
        loanRepo.deleteById(loanId);
        System.out.println("Book returned. Loan ID: " + loanId);
    }
 
    public boolean hasBorrowedBooks() {
        return !loanRepo.findAll().isEmpty();
    }
}
