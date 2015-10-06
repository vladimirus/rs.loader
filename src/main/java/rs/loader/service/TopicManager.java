package rs.loader.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.loader.dao.TopicDao;
import rs.loader.model.Topic;

import java.util.Collection;

@Service
public class TopicManager implements SimpleManager<Topic> {
    private Logger log = Logger.getLogger(TopicManager.class);
    @Autowired
    private TopicDao topicDao;

    @Override
    public void save(Topic topic) {
        topicDao.save(topic);
    }

    @Override
    public void save(Collection<Topic> collection) {
        topicDao.save(collection);
    }

    @Override
    public Collection<Topic> get(int pageNumber, int size) {
        return topicDao.get(pageNumber, size);
    }

    public Collection<Topic> getTop(int size) {
        return topicDao.getTop(size);
    }
}
