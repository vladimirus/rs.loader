package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
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
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(new PageRequest(pageNumber, size))
                .withIndices(INDEX_NAME)
                .withTypes(TYPE)
                .build();

        FacetedPage<Comment> page = template.queryForPage(searchQuery, Comment.class);
        return page.getContent();
    }
}
