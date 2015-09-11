package rs.loader.job;

import com.github.jreddit.entity.Subreddit;
import com.github.jreddit.retrieval.ExtendedComments;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.loader.model.Topic;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

@Service
public class CommentLoaderJob extends AbstractLoaderJob<Subreddit, Topic> {
    private Logger log = Logger.getLogger(CommentLoaderJob.class);
    @Autowired
    private ExtendedComments comments;
    @Autowired
    private Converter<Subreddit, Topic> topicConverter;
    @Autowired
    private Validator<Topic> topicValidator;


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
