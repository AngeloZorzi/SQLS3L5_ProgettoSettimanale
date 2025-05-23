package entities;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Element item;

    private LocalDate loanDate;
    private LocalDate returnDueDate;
    private LocalDate returnDate;

    public Loan() {}

    public Loan(User user, Element item, LocalDate loanDate) {
        this.user = user;
        this.item = item;
        this.loanDate = loanDate;
        this.returnDueDate = loanDate.plusDays(30);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Element getItem() {
        return item;
    }

    public void setItem(Element item) {
        this.item = item;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(LocalDate loanDate) {
        this.loanDate = loanDate;
    }

    public LocalDate getReturnDueDate() {
        return returnDueDate;
    }

    public void setReturnDueDate(LocalDate returnDueDate) {
        this.returnDueDate = returnDueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    @Override
    public String toString() {
        return String.format("Loan of %s to %s | Due: %s | Returned: %s",
                item.getTitle(),
                user.getCardNumber(),
                returnDueDate,
                returnDate != null ? returnDate : "N/A");
    }
}

