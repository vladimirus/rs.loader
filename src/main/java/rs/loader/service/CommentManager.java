package rs.loader.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.loader.dao.CommentDao;
import rs.loader.model.Comment;

import java.util.Collection;

@Service
public class CommentManager implements SimpleManager<Comment> {
    private Logger log = Logger.getLogger(CommentManager.class);
    @Autowired
    private CommentDao commentDao;

    @Override
    public void save(Comment comment) {
        commentDao.save(comment);
    }

    @Override
    public void save(Collection<Comment> collection) {
        commentDao.save(collection);
    }

    @Override
    public Collection<Comment> get(int pageNumber, int size) {
        return commentDao.get(pageNumber, size);
    }

    public Collection<Comment> getCommentsForLinkId(String linkId) {
        return commentDao.getCommentsForLinkId(0, 100000, linkId);
    }
}
