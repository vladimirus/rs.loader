package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.stereotype.Repository;
import rs.loader.model.Link;

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
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Link.class)
                .type(TYPE)
                .index(INDEX_NAME)
                .sortDesc(true)
                .sortField("score")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }
}
