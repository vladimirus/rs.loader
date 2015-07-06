package rs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dao.SimpleDao;
import rs.model.Topic;

@Service
public class TopicManager implements SimpleManager<Topic> {
    @Autowired
    private SimpleDao<Topic> linkDao;

    @Override
    public void save(Topic link) {
        linkDao.save(link);
    }
}
