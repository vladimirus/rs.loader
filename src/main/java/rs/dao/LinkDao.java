package rs.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Repository;
import rs.model.Link;

@Repository
public class LinkDao implements SimpleDao<Link> {
    @Autowired
    private ElasticsearchTemplate template;

    @Override
    public void save(Link link) {
        template.index(indexQuery(link));
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
