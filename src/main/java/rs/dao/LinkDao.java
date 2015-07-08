package rs.dao;

import static org.elasticsearch.index.query.QueryBuilders.matchAllQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Repository;
import rs.model.Link;

import java.util.Collection;

@Repository
public class LinkDao implements SimpleDao<Link> {
    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public void save(Link link) {
        template.index(indexQuery(link));
    }

    @Override
    public Collection<Link> get(int pageNumber, int size) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchAllQuery())
                .withPageable(new PageRequest(pageNumber, size))
                .withIndices("rs")
                .withTypes("link")
                .build();

        FacetedPage<Link> page = template.queryForPage(searchQuery, Link.class);
        return page.getContent();
    }

    private IndexQuery indexQuery(Link link) {
        return new IndexQueryBuilder()
                .withObject(link)
                .withId(link.getId())
                .withIndexName("rs")
                .withType("link")
                .build();
    }
}
