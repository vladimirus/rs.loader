package rs.service;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dao.SimpleDao;
import rs.model.Topic;

import java.util.Collection;

@Service
public class TopicManager implements SimpleManager<Topic> {
    @Autowired
    private SimpleDao<Topic> topicDao;
    @Autowired
    private AsyncEventBus eventBus;

    @Override
    public void save(Topic topic) {
        topicDao.save(topic);
        eventBus.post(topic);
    }

    @Override
    public Collection<Topic> get(int pageNumber, int size) {
        return topicDao.get(pageNumber, size);
    }
}
