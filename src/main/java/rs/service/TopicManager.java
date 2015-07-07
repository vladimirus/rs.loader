package rs.service;

import com.google.common.eventbus.AsyncEventBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dao.SimpleDao;
import rs.model.Topic;

@Service
public class TopicManager implements SimpleManager<Topic> {
    @Autowired
    private SimpleDao<Topic> linkDao;
    @Autowired
    private AsyncEventBus eventBus;

    @Override
    public void save(Topic topic) {
        linkDao.save(topic);
        eventBus.post(topic);
    }
}
