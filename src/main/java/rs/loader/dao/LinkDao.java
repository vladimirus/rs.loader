package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.stereotype.Repository;
import rs.loader.model.Link;

import java.util.Collection;

@Repository
public class LinkDao extends ModelDao<Link> implements SimpleDao<Link> {

    public LinkDao() {
        super("link");
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Link.class)
                .type(getType())
                .index(indexName)
                .sortDesc(true)
                .sortField("score")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }
}
