
import dao.ElementDAO;
import dao.LoanDAO;
import dao.UserDAO;
import entities.Element;
import entities.Loan;
import entities.User;
import jakarta.persistence.EntityManager;

import java.time.LocalDate;
import java.util.List;

public class Archive {
    private final ElementDAO elementDAO;
    private final UserDAO userDAO;
    private final LoanDAO loanDAO;

    public Archive(EntityManager em) {
        this.elementDAO = new ElementDAO(em);
        this.userDAO = new UserDAO(em);
        this.loanDAO = new LoanDAO(em);
    }


    public void addElement(Element element) {
        elementDAO.save(element);
    }

    public boolean removeElementByISBN(String isbn) {
        Element element = elementDAO.findByISBN(isbn);
        if (element != null) {
            elementDAO.delete(isbn);
            return true;
        }
        return false;
    }

    public Element searchByISBN(String isbn) {
        return elementDAO.findByISBN(isbn);
    }

    public List<Element> searchByYear(int year) {
        return elementDAO.findByYear(year);
    }

    public List<Element> searchByTitle(String partialTitle) {
        return elementDAO.findByTitle(partialTitle);
    }

    public List<Element> searchByAuthor(String author) {
        return elementDAO.findByAuthor(author);
    }


    public void addUser(User user) {
        userDAO.save(user);
    }

    public User getUserByCardNumber(String cardNumber) {
        return userDAO.findByCardNumber(cardNumber);
    }


    public void registerLoan(Loan loan) {
        loanDAO.save(loan);
    }

    public List<Loan> getLoansByUserCard(String cardNumber) {
        return loanDAO.findByUserCardNumber(cardNumber);
    }

    public List<Loan> getExpiredAndNotReturnedLoans() {
        return loanDAO.findOverdueLoans();
    }
}

