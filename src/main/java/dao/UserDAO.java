package dao;

import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class UserDAO {
    private final EntityManager em;

    public UserDAO(EntityManager em) {
        this.em = em;
    }

    public void save(User user) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(user);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    public User findByCardNumber(String cardNumber) {
        return em.find(User.class, cardNumber);
    }

    public List<User> findAll() {
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList();
    }

    public void delete(User user) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.remove(em.contains(user) ? user : em.merge(user));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}

