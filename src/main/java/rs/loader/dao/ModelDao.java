package rs.loader.dao;

import static java.util.stream.Collectors.toList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;
import rs.loader.model.Model;

import java.util.Collection;
import java.util.List;

@Repository
public abstract class ModelDao<T extends Model> {
    final static String INDEX_NAME = "rs";

    @Autowired
    ElasticsearchTemplate template;

    void save(T item, String index, String type) {
        template.index(indexQuery(item, index, type));
    }

    void save(Collection<T> collection, String index, String type) {
        List<IndexQuery> queries = collection.stream()
                .map(item -> indexQuery(item, index, type))
                .collect(toList());

        if (!queries.isEmpty()) {
            template.bulkIndex(queries);
        }
    }

    IndexQuery indexQuery(T item, String index, String type) {
        return new IndexQueryBuilder()
                .withObject(item)
                .withId(item.getId())
                .withIndexName(index)
                .withType(type)
                .build();
    }
}
