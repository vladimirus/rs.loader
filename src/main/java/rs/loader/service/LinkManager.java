package rs.loader.service;

import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.loader.dao.SimpleDao;
import rs.loader.model.Link;
import rs.loader.model.Suggestion;
import rs.loader.service.convert.SuggestionConverter;

import java.util.Collection;

@Service
public class LinkManager implements SimpleManager<Link> {
    private Logger log = Logger.getLogger(LinkManager.class);
    @Autowired
    private SimpleDao<Link> linkDao;
    @Autowired
    private SimpleDao<Suggestion> suggestionDao;

    @Autowired
    private SuggestionConverter suggestionConverter;

    @Override
    public void save(Link link) {
        linkDao.save(link);
    }

    @Override
    public void save(Collection<Link> links) {
        linkDao.save(links);

        suggestionDao.save(links.stream()
                .flatMap(link -> suggestionConverter.convert(link).stream())
                .collect(toList()));
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        return linkDao.get(pageNumber, size);
    }
}
