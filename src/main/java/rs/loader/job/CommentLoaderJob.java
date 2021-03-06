package rs.loader.job;

import static com.github.jreddit.retrieval.params.CommentSort.TOP;
import static com.google.common.util.concurrent.Uninterruptibles.sleepUninterruptibly;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.rangeClosed;
import static java.util.stream.Stream.concat;
import static java.util.stream.Stream.of;

import com.github.jreddit.retrieval.Comments;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang.time.StopWatch;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.stream.Stream;

@Service
public class CommentLoaderJob extends AbstractLoaderJob<com.github.jreddit.entity.Comment, Comment> {
    static int IDLE_SLEEP_IN_SECONDS = 60;
    static int ERROR_SLEEP_IN_SECONDS = 10;

    private Logger log = Logger.getLogger(CommentLoaderJob.class);
    @Autowired
    private Comments comments;
    @Autowired
    private Converter<com.github.jreddit.entity.Comment, Comment> commentConverter;
    @Autowired
    private Validator<Comment> commentValidator;
    @Autowired
    private SimpleManager<Comment> commentManager;
    @Autowired
    @Qualifier(value = "commentExecutor")
    private Executor executor;

    Queue<String> queue = new LinkedList<>();

    @Value("${rs.loader.comment.enabled:true}")
    boolean enabled;

    @Scheduled(initialDelay = 20000, fixedRate = 2000)
    public void scheduledLoad() {
        if (enabled) {
            executor.execute(this::load);
        }
    }

    public void load() {
        gaugeService.submit("loader.comment.queue-size", queueSize());

        if(queue.isEmpty()) {
            log.info(format("Comment queue is empty, utilise it more? Sleeping for %d seconds...", IDLE_SLEEP_IN_SECONDS));
            sleepUninterruptibly(IDLE_SLEEP_IN_SECONDS, SECONDS);
        } else {
            StopWatch timer = new StopWatch();
            timer.start();
            ofNullable(queue.poll())
                    .flatMap(linkId -> process(linkId, 200))
                    .filter(comments -> !comments.isEmpty())
                    .ifPresent(comments -> {
                        timer.stop();
                        commentManager.save(comments);
                        log.debug(format("%20s: saved %3d comments; took: %s", comments.stream().findAny().get().getLinkId(), comments.size(), timer.toString()));
                    });
        }
    }

    Optional<Collection<Comment>> process(String link, int maxAttempts) {
        return rangeClosed(1, maxAttempts)
                .mapToObj(i -> {
                    try {
                        return load(comments.ofSubmission(link, null, -1, -1, -1, TOP).stream().flatMap(this::flattened), commentConverter, commentValidator);
                    } catch (Exception ignore) {
                        log.info(format("Error retrieving comments: iteration: %d, link: %s. Sleeping for %d seconds then trying again", i, link, i * ERROR_SLEEP_IN_SECONDS));
                        counterService.increment(format("loader.%s.sleeping", this.getClass().getSimpleName().toLowerCase()));
                        sleepUninterruptibly(i * ERROR_SLEEP_IN_SECONDS, SECONDS);
                        counterService.decrement(format("loader.%s.sleeping", this.getClass().getSimpleName().toLowerCase()));
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
        if (enabled) {
            queue.add(link.getIdWithoutType());
        }
    }

    public int queueSize() {
        return queue.size();
    }
}
