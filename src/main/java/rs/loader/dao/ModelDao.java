package rs.loader.dao;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.ASC;
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

    @SuppressWarnings("unchecked")
    public Collection<T> get(RsQuery query) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(query.getQueryBuilder())
                .withSort(fieldSort(query.getSortField()).order(query.getSortDesc() ? DESC : ASC))
                .withPageable(new PageRequest(query.getPageNumber(), query.getSize()))
                .withIndices(query.getIndex())
                .withTypes(query.getType())
                .build();

        FacetedPage<T> page = template.queryForPage(searchQuery, query.getClazz());
        return page.getContent();
    }
}
