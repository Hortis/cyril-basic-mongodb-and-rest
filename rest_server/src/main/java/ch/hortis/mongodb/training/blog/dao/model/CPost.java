package ch.hortis.mongodb.training.blog.dao.model;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.jongo.marshall.jackson.oid.Id;

public class CPost {

    public static final String COLLECTION_NAME = "post";
    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String AUTHOR = "author";
    private final String title;
    private final String description;
    private final String author;
    @Id
    private String postId;

    @JsonCreator
    public CPost(@JsonProperty(ID) String postId, @JsonProperty(TITLE) String title, @JsonProperty(DESCRIPTION) String description, @JsonProperty(AUTHOR) String author) {
        this.author = author;
        this.description = description;
        this.postId = postId;
        this.title = title;
    }

    @JsonProperty(ID)
    public String getPostId() {
        return postId;
    }

    @JsonProperty(TITLE)
    public String getTitle() {
        return title;
    }

    @JsonProperty(DESCRIPTION)
    public String getDescription() {
        return description;
    }

    @JsonProperty(AUTHOR)
    public String getAuthor() {
        return author;
    }
}
