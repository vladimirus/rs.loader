package rs.service;

import com.google.common.eventbus.Subscribe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dao.SimpleDao;
import rs.model.Link;

@Service
public class LinkManager implements SimpleManager<Link> {
    @Autowired
    private SimpleDao<Link> linkDao;

    @Override
    @Subscribe
    public void save(Link link) {
        linkDao.save(link);
    }
}
