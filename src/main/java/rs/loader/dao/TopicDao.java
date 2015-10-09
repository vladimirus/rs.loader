package rs.loader.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.stereotype.Repository;
import rs.loader.model.Topic;

import java.util.Collection;

@Repository
public class TopicDao extends ModelDao<Topic> implements SimpleDao<Topic> {

    public TopicDao() {
        super("rs", "topic");
    }

    @Override
    public Collection<Topic> get(int pageNumber, int size) {
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Topic.class)
                .type(getType())
                .index(getIndexName())
                .sortDesc(true)
                .sortField("updated")
                .pageNumber(pageNumber)
                .size(size)
                .build());
    }

    public Collection<Topic> getTop(int size) {
        return get(RsQuery.builder()
                .queryBuilder(matchAllQuery())
                .clazz(Topic.class)
                .type(getType())
                .index(getIndexName())
                .sortDesc(true)
                .sortField("subscribers")
                .pageNumber(0)
                .size(size)
                .build());
    }
}
