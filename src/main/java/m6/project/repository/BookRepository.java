package m6.project.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import m6.project.model.Book;

public class BookRepository implements Repository<Book, Integer> {  
    private final EntityManager em;

    public BookRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Book save(Book entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }

    @Override
    public void delete(Book entity) {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Book book = findById(id);
        if (book != null) delete(book);
    }

    @Override
    public Book findById(Integer id) {
        return em.find(Book.class, id);
    }

    @Override
    public List<Book> findAll() {
        return em.createQuery("SELECT b FROM Book b", Book.class).getResultList();
    }

     
    public List<Book> findAvailableBooks() {
        return em.createQuery("SELECT b FROM Book b WHERE b.isAvailable = true", Book.class).getResultList();
    }
}
