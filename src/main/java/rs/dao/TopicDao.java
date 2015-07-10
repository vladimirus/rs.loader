package rs.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import rs.model.Topic;

import java.util.Collection;

@Repository
public class TopicDao extends AbstractDao<Topic> implements SimpleDao<Topic> {
    private final static String TYPE = "topic";

    @Override
    public void save(Topic topic) {
        save(topic, INDEX_NAME, TYPE);
    }

    @Override
    public void save(Collection<Topic> collection) {
        save(collection, INDEX_NAME, TYPE);
    }

    @Override
    public Collection<Topic> get(int pageNumber, int size) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(fieldSort("updated").order(DESC))
                .withPageable(new PageRequest(pageNumber, size))
                .withIndices(INDEX_NAME)
                .withTypes(TYPE)
                .build();

        FacetedPage<Topic> page = template.queryForPage(searchQuery, Topic.class);
        return page.getContent();
    }
}
