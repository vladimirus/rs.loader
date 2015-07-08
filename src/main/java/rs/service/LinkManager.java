package rs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.dao.SimpleDao;
import rs.model.Link;

import java.util.Collection;

@Service
public class LinkManager implements SimpleManager<Link> {
    @Autowired
    private SimpleDao<Link> linkDao;

    @Override
    public void save(Link link) {
        linkDao.save(link);
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        return linkDao.get(pageNumber, size);
    }
}
