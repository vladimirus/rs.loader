package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.elasticsearch.index.query.IdsQueryBuilder;
import org.springframework.stereotype.Repository;
import rs.loader.model.Suggestion;

import java.util.Collection;

@Repository
public class SuggestionDao extends ModelDao<Suggestion> implements SimpleDao<Suggestion> {

    public SuggestionDao() {
        super("suggestion");
    }

    @Override
    public Collection<Suggestion> get(int pageNumber, int size) {
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Suggestion.class)
                .type(getType())
                .index(indexName)
                .sortDesc(true)
                .sortField("original")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }

    public Collection<Suggestion> getById(String id) {
        return get(RsQuery.builder()
                .queryBuilder(new IdsQueryBuilder(getType()).ids(id))
                .clazz(Suggestion.class)
                .type(getType())
                .index(indexName)
                .sortDesc(true)
                .sortField("original")
                .pageNumber(0)
                .size(1)
                .build());
    }
}
