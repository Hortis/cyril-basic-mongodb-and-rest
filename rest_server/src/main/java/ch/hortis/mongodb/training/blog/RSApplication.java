package ch.hortis.mongodb.training.blog;


import org.glassfish.jersey.server.ResourceConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//@Component
public class RSApplication extends ResourceConfig {

    private Logger logger = LoggerFactory.getLogger(getClass());


    public RSApplication() {
        logger.info("RSApplication()");
        packages("ch.hortis.mongodb.training.blog.web", "ch.hortis.mongodb.training.blog.oauth");
        //register(PostResourceImpl.class);
    }

}
