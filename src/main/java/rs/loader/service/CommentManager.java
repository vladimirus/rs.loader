package rs.loader.service;

import static java.util.Collections.singletonList;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.loader.dao.LinkDao;
import rs.loader.dao.SimpleDao;
import rs.loader.model.Comment;

import java.util.Collection;

@Service
public class CommentManager implements SimpleManager<Comment> {
    private Logger log = Logger.getLogger(CommentManager.class);
    @Autowired
    private SimpleDao<Comment> commentDao;
    @Autowired
    private LinkDao linkDao;

    @Override
    public void save(Comment comment) {
        commentDao.save(comment);
        linkDao.updateComments(singletonList(comment));
    }

    @Override
    public void save(Collection<Comment> collection) {
        commentDao.save(collection);
        linkDao.updateComments(collection);
    }

    @Override
    public Collection<Comment> get(int pageNumber, int size) {
        return commentDao.get(pageNumber, size);
    }
}
