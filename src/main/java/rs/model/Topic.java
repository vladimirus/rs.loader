package rs.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document(indexName = "test")
public class Topic {
    @NonNull
    private String id;
}
