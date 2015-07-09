package rs.service;

import static java.lang.String.format;

import com.google.common.eventbus.AsyncEventBus;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dao.SimpleDao;
import rs.model.Topic;

import java.util.Collection;

@Service
public class TopicManager implements SimpleManager<Topic> {
    private Logger log = Logger.getLogger(TopicManager.class);
    @Autowired
    private SimpleDao<Topic> topicDao;
    @Autowired
    private AsyncEventBus eventBus;

    @Override
    public void save(Topic topic) {
        log.debug(format("Saving topic %s, subscribers: %d", topic.getDisplayName(), topic.getSubscribers()));
        topicDao.save(topic);
        eventBus.post(topic);
    }

    @Override
    public Collection<Topic> get(int pageNumber, int size) {
        return topicDao.get(pageNumber, size);
    }
}
