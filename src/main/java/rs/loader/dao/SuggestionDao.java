package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.stereotype.Repository;
import rs.loader.model.Suggestion;

import java.util.Collection;

@Repository
public class SuggestionDao extends ModelDao<Suggestion> implements SimpleDao<Suggestion> {

    public SuggestionDao() {
        super("rs", "suggestion");
    }

    @Override
    public Collection<Suggestion> get(int pageNumber, int size) {
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Suggestion.class)
                .type(getType())
                .index(getIndexName())
                .sortDesc(true)
                .sortField("original")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }
}
