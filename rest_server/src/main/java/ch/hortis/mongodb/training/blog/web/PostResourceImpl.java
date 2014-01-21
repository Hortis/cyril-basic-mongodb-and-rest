package ch.hortis.mongodb.training.blog.web;


import ch.hortis.mongodb.training.blog.dao.PostDao;
import ch.hortis.mongodb.training.blog.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Path("post")
@Component
@RolesAllowed(value = {"ROLE_CLIENT"})
public class PostResourceImpl implements PostResource {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostDao postDao;


    public PostResourceImpl() {
        logger.info("create PostResourceImpl");
       // createData();
    }

    @GET
    @Produces(value = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    //@Path("all/{start}/{end}")
    @Override
    //public List<Post> findAll(@PathParam("start")int start, @PathParam("end")int end) {
    public List<Post> findAll() {
        logger.info("findAll called");
        //return postDao.findAll(start, end);
        return postDao.findAll(0, 10);
    }

    @GET
    @Path("{post_id}")
    @Produces(value = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Response get(@PathParam("post_id") String id) {
        try {
            logger.info("Get detail of {}", id);
            return Response.ok(postDao.getPost(id), MediaType.APPLICATION_JSON).build();
        }catch(IllegalStateException ex) {
            return Response.status(Response.Status.NOT_FOUND).entity("Entity not found for id: " + id).build();
        }
    }

    @POST
    @Path("{post_id}")
    @Consumes(value = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Produces(value = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public Post update(@PathParam("post_id") String id, Post post) {
        return postDao.createOrUpdatePost(post);
    }

    @PUT
    @Consumes(value = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @Override
    public void create(Post post) {
        logger.info("create new post {}", post.getTitle());
        postDao.createOrUpdatePost(post);
    }

    @DELETE
    @Path("{post_id}")
    @Produces(value = {MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    @RolesAllowed(value = {"ROLE_TRUSTED_CLIENT"})
    @Override
    public Post delete(@PathParam("post_id") String id) {
        logger.info("remove post with id {}", id);
        return postDao.delete(id);
    }
}
