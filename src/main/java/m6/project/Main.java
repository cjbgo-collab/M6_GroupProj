package m6.project;

import jakarta.persistence.EntityManager;              
import m6.util.JPAUtil;                            
import m6.project.service.BookService;                
import m6.project.service.LoanService;           
import m6.project.service.UserService;                

public class Main {

    public static void main(String[] args) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            BookService bookService = new BookService(em);   
            LoanService loanService = new LoanService(em);  
            UserService userService = new UserService(em);   

            LibraryApplication app = new LibraryApplication(bookService, loanService, userService); 
            app.start();

        } catch (Exception e) {
            e.printStackTrace(); 
        } finally {
            if (em != null && em.isOpen()) {
                em.close(); 
            }
        }
    }
}
