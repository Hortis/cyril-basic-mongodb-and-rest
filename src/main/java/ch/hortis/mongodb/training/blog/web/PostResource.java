package ch.hortis.mongodb.training.blog.web;

import ch.hortis.mongodb.training.blog.model.Post;

import javax.ws.rs.core.Response;
import java.util.List;


public interface PostResource {


    List<Post> findAll();

    Response get(String id);

    Post update(String id, Post post);

    void create(Post post);

    Post delete(String id);


}
