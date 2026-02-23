package m6.project.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import m6.project.model.Loan;

public class LoanRepository implements Repository<Loan, Integer> {  
    private EntityManager em;

    public LoanRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public Loan save(Loan entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }

    @Override
    public void delete(Loan entity) {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        Loan loan = findById(id);
        if (loan != null) delete(loan);
    }

    @Override
    public Loan findById(Integer id) {
        return em.find(Loan.class, id);
    }

    @Override
    public List<Loan> findAll() {
        return em.createQuery("SELECT l FROM Loan l", Loan.class).getResultList();
    }
}
