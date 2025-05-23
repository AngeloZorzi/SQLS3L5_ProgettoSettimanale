package entities;

import jakarta.persistence.Entity;

@Entity
public class Book extends Element{
    private String author;
    private String genre;

    public Book(){}

    public Book(String ISBN, String title, int year, int pages, String author, String genre) {
        super(ISBN, title, year, pages);
        this.author = author;
        this.genre = genre;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Book{" +
                "author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
