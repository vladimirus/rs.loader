package rs.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import rs.model.Topic;

import java.util.Collection;

@Repository
public class TopicDao implements SimpleDao<Topic> {
    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public void save(Topic topic) {
        template.index(indexQuery(topic));
    }

    @Override
    public Collection<Topic> get(int pageNumber, int size) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withSort(fieldSort("updated").order(DESC))
                .withPageable(new PageRequest(pageNumber, size))
                .withIndices("rs")
                .withTypes("topic")
                .build();

        FacetedPage<Topic> page = template.queryForPage(searchQuery, Topic.class);
        return page.getContent();
    }

    private IndexQuery indexQuery(Topic topic) {
        return new IndexQueryBuilder()
                .withObject(topic)
                .withId(topic.getId())
                .withIndexName("rs")
                .withType("topic")
                .build();
    }


}
