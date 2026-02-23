package m6.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "books")
public class Book { 
    @Id
    private Integer bookId;

    @Column(nullable = false)
    private String bookTitle;

    private String bookAuthor;

    @Column(nullable = false)
    private boolean isAvailable = true;
   
    public Integer getBookId() { return bookId; }
    public void setBookId(Integer bookId) { this.bookId = bookId; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { this.isAvailable = available; }
}
