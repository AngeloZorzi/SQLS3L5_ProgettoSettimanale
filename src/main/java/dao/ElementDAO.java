package dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;

import entities.Element;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ElementDAO {
    private EntityManager em;
    public ElementDAO(EntityManager em){
        this.em = em;
    }
    public void save(Element element) {
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        em.persist(element);
        tx.commit();
    }
    public Element findByISBN(String isbn) {
        return em.find(Element.class, isbn);
    }
    public void delete(String isbn) {
        Element found = findByISBN(isbn);
        if (found != null) {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            em.remove(found);
            tx.commit();
        }
    }
    public List<Element> findByYear(int year) {
        return em.createQuery("SELECT e FROM Element e WHERE e.year = :year", Element.class)
                .setParameter("year", year)
                .getResultList();
    }

    public List<Element> findByTitle(String titlePart) {
        return em.createQuery("SELECT e FROM Element e WHERE LOWER(e.title) LIKE LOWER(:title)", Element.class)
                .setParameter("title", "%" + titlePart + "%")
                .getResultList();
    }
    public List<Element> findByAuthor(String author) {
        return em.createQuery("SELECT e FROM Book e WHERE e.author = :author", Element.class)
                .setParameter("author", author)
                .getResultList();
    }

}
