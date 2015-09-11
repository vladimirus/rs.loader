package rs.loader.job;

import com.github.jreddit.retrieval.ExtendedComments;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.loader.model.Comment;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

@Service
public class CommentLoaderJob extends AbstractLoaderJob<com.github.jreddit.entity.Comment, Comment> {
    private Logger log = Logger.getLogger(CommentLoaderJob.class);
    @Autowired
    private ExtendedComments comments;
    @Autowired
    private Converter<com.github.jreddit.entity.Comment, Comment> topicConverter;
    @Autowired
    private Validator<Comment> commentValidator;


//    @Scheduled(initialDelay = 20000, fixedRate = 1000)
    public synchronized void load() {

    }


//    Optional<Collection<Topic>> process(Optional<Link> linkToCheck, int maxAttempts) {
//        linkToCheck.ifPresent(link -> {
//            log.debug(format("Retrieving comments, link: %s", link.getId()));
//        });
//
//        return rangeClosed(1, maxAttempts)
//                .mapToObj(i -> {
//                    try {
//                        return load(comments.ofSubmission(), topicConverter, topicValidator);
//                    } catch (Exception ignore) {
//                        log.info(format("Error retrieving comments. Trying again, iteration: %d, link: %s", i, linkToCheck.map(Link::getId).orElse("<null>")));
//                        sleepUninterruptibly(2, SECONDS);
//                        return null;
//                    }
//                })
//                .filter(topics -> topics != null)
//                .findAny();
//    }

}
