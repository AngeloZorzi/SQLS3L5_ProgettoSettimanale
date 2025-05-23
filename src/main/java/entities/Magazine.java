package entities;

import enumerating.Periodicity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Magazine extends Element{
    @Enumerated(EnumType.STRING)
    private Periodicity periodicity;

    public Magazine(){}

    public Magazine(String ISBN, String title, int year, int pages, Periodicity periodicity) {
        super(ISBN, title, year, pages);
        this.periodicity = periodicity;
    }

    public Periodicity getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(Periodicity periodicity) {
        this.periodicity = periodicity;
    }

    @Override
    public String toString() {
        return "Magazine{" +
                "periodicity=" + periodicity +
                '}';
    }
}
