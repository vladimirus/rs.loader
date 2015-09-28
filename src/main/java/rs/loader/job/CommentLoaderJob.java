package rs.loader.job;

import static com.github.jreddit.retrieval.params.CommentSort.TOP;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.rangeClosed;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

import com.github.jreddit.retrieval.ExtendedComments;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import rs.loader.model.Comment;
import rs.loader.model.Link;
import rs.loader.service.SimpleManager;
import rs.loader.service.convert.Converter;
import rs.loader.service.validator.Validator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;

@Service
public class CommentLoaderJob extends AbstractLoaderJob<com.github.jreddit.entity.Comment, Comment> {
    static int IDLE_SLEEP_IN_SECONDS = 60;
    static int ERROR_SLEEP_IN_SECONDS = 10;

    private Logger log = Logger.getLogger(CommentLoaderJob.class);
    @Autowired
    private ExtendedComments comments;
    @Autowired
    private Converter<com.github.jreddit.entity.Comment, Comment> commentConverter;
    @Autowired
    private Validator<Comment> commentValidator;
    @Autowired
    private SimpleManager<Comment> commentManager;
    @Autowired
    private GaugeService gaugeService;

    Queue<String> queue = new LinkedList<>();

    @Scheduled(initialDelay = 20000, fixedRate = 100)
    public synchronized void load() {
        gaugeService.submit("loader.comment.queue-size", queueSize());

        StopWatch timer = new StopWatch();
        timer.start();

        ofNullable(queue.poll())
                .flatMap(linkId -> process(linkId, 10))
                .filter(comments -> !comments.isEmpty())
                .ifPresent(comments -> {
                    timer.stop();
                    commentManager.save(comments);
                    log.debug(format("%20s: saved %3d comments; took: %s", comments.stream().findAny().get().getLinkId(), comments.size(), timer.toString()));
                });

        if(queue.isEmpty()) {
            log.info(format("Comment queue is empty, utilise it more? Sleeping for %d seconds...", IDLE_SLEEP_IN_SECONDS));
            sleepUninterruptibly(IDLE_SLEEP_IN_SECONDS, SECONDS);
        }

    }

    Optional<Collection<Comment>> process(String link, int maxAttempts) {
        log.debug(format("Retrieving comments for link: %s", link));

        return rangeClosed(1, maxAttempts)
                .mapToObj(i -> {
                    try {

                        StopWatch timer = new StopWatch();
                        timer.start();
                        List<com.github.jreddit.entity.Comment> c = comments.ofSubmission(link, TOP, -1, null);
                        timer.split();
                        log.info(format("comments for %s retrieved in %s", link, timer.toString()));
                        List<com.github.jreddit.entity.Comment> cc = c.stream().flatMap(this::flattened).collect(toList());
                        timer.stop();
                        log.info(format("comments for %s flattened in %s", link, timer.toString()));

                        return load(cc.parallelStream(), commentConverter, commentValidator);
                    } catch (Exception ignore) {
                        log.info(format("Error retrieving comments. Trying again, iteration: %d, link: %s", i, link));
                        sleepUninterruptibly(ERROR_SLEEP_IN_SECONDS, SECONDS);
                        return null;
                    }
                })
                .filter(comments -> comments != null)
                .findAny();
    }

    public Stream<com.github.jreddit.entity.Comment> flattened(com.github.jreddit.entity.Comment comment) {
        return concat(of(comment), ofNullable(comment.getReplies()).orElse(new ArrayList<>()).stream().flatMap(this::flattened));
    }

    @Subscribe
    public void handle(Link link) {
        queue.add(link.getIdWithoutType());
    }

    public int queueSize() {
        return queue.size();
    }
}
