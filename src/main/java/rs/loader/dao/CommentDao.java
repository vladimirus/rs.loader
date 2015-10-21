package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.stereotype.Repository;
import rs.loader.model.Comment;

import java.util.Collection;

@Repository
public class CommentDao extends ModelDao<Comment> implements SimpleDao<Comment> {

    public CommentDao() {
        super("comment");
    }

    @Override
    public Collection<Comment> get(int pageNumber, int size) {
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Comment.class)
                .type(getType())
                .index(indexName)
                .sortDesc(true)
                .sortField("score")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }
}
