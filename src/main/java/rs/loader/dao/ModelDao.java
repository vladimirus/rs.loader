package rs.loader.dao;

import static java.util.stream.Collectors.toList;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.ASC;
import static org.elasticsearch.search.sort.SortOrder.DESC;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public abstract class ModelDao<T extends Model> implements SimpleDao<T>  {
    @Getter
    private final String type;
    @Value("${rs.index.name}")
    String indexName;

    @Autowired
    ElasticsearchTemplate template;

    public ModelDao(String type) {
        this.type = type;
    }

    public void save(T item) {
        template.index(indexQuery(item, indexName, type));
    }

    public void save(Collection<T> collection) {
        List<IndexQuery> queries = collection.stream()
                .map(item -> indexQuery(item, indexName, type))
                .collect(toList());

        if (!queries.isEmpty()) {
            template.bulkIndex(queries);
        }
    }

    public IndexQuery indexQuery(T item, String index, String type) {
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
                .withFilter(query.getFilter())
                .withSort(fieldSort(query.getSortField()).order(query.getSortDesc() ? DESC : ASC))
                .withPageable(new PageRequest(query.getPageNumber(), query.getSize()))
                .withIndices(query.getIndex())
                .withTypes(query.getType())
                .build();

        FacetedPage<T> page = template.queryForPage(searchQuery, query.getClazz());
        return page.getContent();
    }
}
