package rs.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dao.SimpleDao;
import rs.model.Link;

import java.util.Collection;

@Service
public class LinkManager implements SimpleManager<Link> {
    private Logger log = Logger.getLogger(LinkManager.class);
    @Autowired
    private SimpleDao<Link> linkDao;

    @Override
    public void save(Link link) {
        linkDao.save(link);
    }

    @Override
    public void save(Collection<Link> collection) {
        linkDao.save(collection);
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        return linkDao.get(pageNumber, size);
    }
}
