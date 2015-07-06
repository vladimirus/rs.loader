package rs.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;
import rs.model.Topic;

@Repository
public class TopicDao implements SimpleDao<Topic> {
    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public void save(Topic topic) {
        template.index(indexQuery(topic));
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
