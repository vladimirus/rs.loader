package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.stereotype.Repository;
import rs.loader.model.Comment;

import java.util.Collection;

@Repository
public class CommentDao extends ModelDao<Comment> implements SimpleDao<Comment> {
    private final static String TYPE = "comment";

    @Override
    public void save(Comment comment) {
        save(comment, INDEX_NAME, TYPE);
    }

    @Override
    public void save(Collection<Comment> collection) {
        save(collection, INDEX_NAME, TYPE);
    }

    @Override
    public Collection<Comment> get(int pageNumber, int size) {
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Comment.class)
                .type(TYPE)
                .index(INDEX_NAME)
                .sortDesc(true)
                .sortField("score")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }
}
