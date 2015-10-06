package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.stereotype.Repository;
import rs.loader.model.Comment;
import rs.loader.model.Topic;

import java.util.Collection;

@Repository
public class TopicDao extends ModelDao<Topic> implements SimpleDao<Topic> {
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
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Comment.class)
                .type(TYPE)
                .index(INDEX_NAME)
                .sortDesc(true)
                .sortField("updated")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }
}
