package rs.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import rs.model.Link;

import java.util.Collection;

@Repository
public class LinkDao extends ModelDao<Link> implements SimpleDao<Link> {
    private final static String TYPE = "link";

    @Override
    public void save(Link link) {
        save(link, INDEX_NAME, TYPE);
    }

    @Override
    public void save(Collection<Link> collection) {
        save(collection, INDEX_NAME, TYPE);
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(new PageRequest(pageNumber, size))
                .withIndices(INDEX_NAME)
                .withTypes(TYPE)
                .build();

        FacetedPage<Link> page = template.queryForPage(searchQuery, Link.class);
        return page.getContent();
    }
}
