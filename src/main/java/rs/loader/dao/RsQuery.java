package rs.loader.dao;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.elasticsearch.index.query.QueryBuilder;

@Builder
@Getter
public class RsQuery {
    @NonNull
    QueryBuilder queryBuilder;
    @NonNull
    private Integer pageNumber;
    @NonNull
    private Integer size;
    @NonNull
    private Class clazz;
    @NonNull
    private String index;
    @NonNull
    private String type;
    @NonNull
    private String sortField;
    @NonNull
    private Boolean sortDesc;
}
