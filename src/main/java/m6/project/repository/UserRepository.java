package m6.project.repository;

import jakarta.persistence.EntityManager;
import java.util.List;
import m6.project.model.User;

public class UserRepository implements Repository<User, Integer> {  
    private EntityManager em;

    public UserRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    public User save(User entity) {
        em.getTransaction().begin();
        em.persist(entity);
        em.getTransaction().commit();
        return entity;
    }

    @Override
    public void delete(User entity) {
        em.getTransaction().begin();
        em.remove(entity);
        em.getTransaction().commit();
    }

    @Override
    public void deleteById(Integer id) {
        User user = findById(id);
        if (user != null) delete(user);
    }

    @Override
    public User findById(Integer id) {
        return em.find(User.class, id);
    }

    @Override
    public List<User> findAll() {
        return em.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    
    public User findByName(String name) {
        List<User> users = em.createQuery("SELECT u FROM User u WHERE u.userName = :name", User.class)
                             .setParameter("name", name)
                             .getResultList();
        return users.isEmpty() ? null : users.get(0);
    }
}
