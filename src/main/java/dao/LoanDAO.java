package dao;

import entities.Loan;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.util.List;

public class LoanDAO {
    private final EntityManager em;

    public LoanDAO(EntityManager em) {
        this.em = em;
    }

    public void save(Loan loan) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(loan);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    public List<Loan> findByUserCardNumber(String cardNumber) {
        TypedQuery<Loan> query = em.createQuery(
                "SELECT l FROM Loan l WHERE l.user.cardNumber = :cardNumber AND l.returnDate IS NULL",
                Loan.class
        );
        query.setParameter("cardNumber", cardNumber);
        return query.getResultList();
    }

    public List<Loan> findOverdueLoans() {
        TypedQuery<Loan> query = em.createQuery(
                "SELECT l FROM Loan l WHERE l.returnDueDate < :today AND l.returnDate IS NULL",
                Loan.class
        );
        query.setParameter("today", LocalDate.now());
        return query.getResultList();
    }

    public void update(Loan loan) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(loan);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }
}

