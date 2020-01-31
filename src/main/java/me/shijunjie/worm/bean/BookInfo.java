package me.shijunjie.worm.bean;

public class BookInfo {
    private String bookName;
    private String link;
    private String author;
    private String curChapters;
    private String bookStatus;
    private String totalWordNumber;

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCurChapters() {
        return curChapters;
    }

    public void setCurChapters(String curChapters) {
        this.curChapters = curChapters;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }

    public String getTotalWordNumber() {
        return totalWordNumber;
    }

    public void setTotalWordNumber(String totalWordNumber) {
        this.totalWordNumber = totalWordNumber;
    }
}
