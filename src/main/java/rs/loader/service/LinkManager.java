package rs.loader.service;

import static java.util.stream.Collectors.toList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.loader.dao.LinkDao;
import rs.loader.dao.SimpleDao;
import rs.loader.model.Comment;
import rs.loader.model.Link;
import rs.loader.model.Suggestion;
import rs.loader.service.convert.SuggestionConverter;

import java.util.Collection;

@Service
public class LinkManager implements SimpleManager<Link> {
    private Logger log = Logger.getLogger(LinkManager.class);
    @Autowired
    private LinkDao linkDao;
    @Autowired
    private SimpleDao<Suggestion> suggestionDao;
    @Autowired
    private SuggestionConverter suggestionConverter;
    @Autowired
    private CommentManager commentManager;

    @Override
    public void save(Link link) {
        linkDao.save(populateLink(link));
    }

    @Override
    public void save(Collection<Link> links) {
        links.stream().forEach(this::populateLink);
        linkDao.save(links);
        suggestionDao.save(links.stream()
                .flatMap(link -> suggestionConverter.convert(link).stream())
                .collect(toList()));
    }

    private Link populateLink(Link link) {
        Collection<Comment> comments = commentManager.getCommentsForLinkId(link.getId());
        link.setCommentsBody(
                comments.stream()
                        .map(Comment::getBody)
                        .reduce((a, b) -> a + " " + b).orElse("")
        );
        link.setComments(comments.stream()
                .limit(1)
                .collect(toList()));
        return link;
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        return linkDao.get(pageNumber, size);
    }

    public Collection<Link> getMissingComments(int pageNumber, int size) {
        return linkDao.getMissingComments(pageNumber, size);
    }
}
