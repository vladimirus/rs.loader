package rs.service;

import org.springframework.stereotype.Service;
import rs.dao.LinkDao;
import rs.model.Link;

@Service
public class DefaultLinkManager implements LinkManager {
    private LinkDao linkDao;

    @Override
    public void save(Link link) {
        linkDao.save(link);
    }
}
