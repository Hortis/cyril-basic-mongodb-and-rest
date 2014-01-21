package ch.hortis.mongodb.training.blog.model;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Post {

    private String postId;
    private String title;
    private String description;
    private String author;

    public Post() {
    }

    public Post(String postId, String title, String description, String author) {
        this.postId = postId;
        this.title = title;
        this.description = description;
        this.author = author;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
