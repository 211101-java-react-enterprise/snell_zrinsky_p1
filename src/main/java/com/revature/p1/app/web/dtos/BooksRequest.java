package com.revature.p1.app.web.dtos;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BooksRequest {

    private String uuid;
    private String title;
    private String author;
    private int pageCount;
    private String coverImage;

    public BooksRequest(String uuid, String title, String author, int pageCount, String coverImage) {
        this.uuid = uuid;
        this.title = title;
        this.author = author;
        this.pageCount = pageCount;
        this.coverImage = coverImage;
    }

    public BooksRequest(){
        super();
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    @Override
    public String toString() {
        return "BooksRequest{" +
                "uuid='" + uuid + '\'' +
                "title='" + title + '\'' +
                "author='" + author + '\'' +
                "pageCount='" + pageCount + '\'' +
                "coverImage='" + coverImage + '\'' +
                "}";
    }
}
