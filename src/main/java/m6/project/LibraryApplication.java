package m6.project;

import java.util.Scanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import m6.project.service.BookService;
import m6.project.service.LoanService;
import m6.project.service.UserService;
import m6.project.model.User;

public class LibraryApplication {
    private final BookService bookService;
    private final LoanService loanService;
    private final UserService userService;
    private final Scanner input = new Scanner(System.in);
    private boolean running = true;
    private User user;

    private static final Logger logger = LoggerFactory.getLogger(LibraryApplication.class);

    public LibraryApplication(BookService bookService, LoanService loanService, UserService userService) {
        this.bookService = bookService;
        this.loanService = loanService;
        this.userService = userService;
    }

    public void start() {
    	System.out.println("Name: ");
        String name = input.nextLine();
        this.user = userService.loginUser(name);

        while (running) {
            try {
                System.out.println("===================================");
                System.out.println("==      LIBRARY MANAGEMENT       ==");
                System.out.println("===================================");
                System.out.println("[1] DISPLAY ALL BOOKS");
                System.out.println("[2] DISPLAY AVAILABLE BOOKS");
                System.out.println("[3] DISPLAY ALL BORROWED BOOKS");
                System.out.println("[4] BORROW BOOK");
                System.out.println("[5] RETURN BOOK");
                System.out.println("[6] ADD BOOK");
                System.out.println("[7] REMOVE BOOK");
                System.out.println("[8] UPDATE BOOK");
                System.out.println("[0] EXIT");
                System.out.print("ENTER OPTION: ");

                int option;
                try {
                    option = Integer.parseInt(input.nextLine());
                } catch (NumberFormatException e) {
                    logger.warn("Invalid input; should be numeric.");
                    continue;
                }

                switch (option) {
                    case 1:
                        bookService.displayAllBooks();
                        break;
                    case 2:
                        bookService.displayAvailableBooks();
                        break;
                    case 3:
                        loanService.displayAllBorrowedBooks();
                        break;
                    case 4:
                        bookService.displayAvailableBooks();
                        if (bookService.hasAvailableBooks()) {
                            loanService.borrowBook(input, user);
                        } else {
                            logger.info("No available books to borrow.");
                        }
                        break;
                    case 5:
                        loanService.displayAllBorrowedBooks();
                        if (loanService.hasBorrowedBooks()) {
                            loanService.returnBook(input);
                        } else {
                            logger.info("No borrowed books to return.");
                        }
                        break;
                    case 6:
                        bookService.addBook(input);
                        break;
                    case 7:
                        bookService.displayAllBooks();
                        if (bookService.hasAnyBooks()) {
                            bookService.removeBook(input);
                        } else {
                            logger.info("No books to remove.");
                        }
                        break;
                    case 8:
                        bookService.displayAllBooks();
                        if (bookService.hasAnyBooks()) {
                            bookService.updateBook(input);
                        } else {
                            logger.info("No books to update.");
                        }
                        break;
                    case 0:
                        running = false;
                        logger.info("Program terminated.");
                        System.exit(0);
                        break;
                    default:
                        throw new Exception("Invalid option. Please try again.\n");
                }

            } catch (Exception e) {
                logger.error("Unexpected error", e);
            }
        }
    }
}
