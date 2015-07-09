package rs.service;

import static java.lang.String.format;

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
        log.debug(format("Saving link %s, score: %d, comments: %d", link.getUrl(), link.getScore(), link.getCommentCount()));
        linkDao.save(link);
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        return linkDao.get(pageNumber, size);
    }
}
