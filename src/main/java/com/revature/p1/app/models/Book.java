package com.revature.p1.app.models;

// TODO - Make sure this accurately reflects the user's experience
import com.revature.p1.orm.annotations.Column;
import com.revature.p1.orm.annotations.Table;
import com.revature.p1.orm.annotations.types.ColumnType;
import java.util.UUID;

// TODO - Default name logic
@Table(name = "books")
public class Book {
    @Column(name = "id", type = ColumnType.ID, isUnique = true)
    private String id;
    @Column(name = "title", type = ColumnType.STRING)
    private String title;
    @Column(name = "author", type = ColumnType.STRING)
    private String author;
    @Column(name = "page_count", type = ColumnType.INT)
    private int pageCount;
    @Column(name = "cover_image", type = ColumnType.STRING)
    private String coverImage;

    public Book(String id, String title, String author, Integer pageCount, String coverImage) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.pageCount = pageCount;
        this.coverImage = coverImage;
    }

    public Book(String title, String author, Integer pageCount, String coverImage){
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.author = author;
        this.pageCount = pageCount;
        this.coverImage = coverImage;
    }

    public Book() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
}
