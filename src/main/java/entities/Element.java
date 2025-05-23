package entities;

import jakarta.persistence.*;


import java.util.Objects;
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Element {
    @Id
    @Column(name = "id")
    private String ISBN;
    private String title;
    private int year;
    private int pages;

    public Element(){}

    public Element(String ISBN, String title, int year, int pages){
        this.ISBN = ISBN;
        this.title = title;
        this.year = year;
        this.pages = pages;
    }

    public String getISBN() {
        return ISBN;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public int getPages() {
        return pages;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
    @Override
    public String toString() {
        return String.format("ISBN: %s | Title: %s | Year: %d | Pages: %d", ISBN, title, year, pages);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Element element = (Element) o;
        return ISBN.equals(element.ISBN);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ISBN);
    }
}
