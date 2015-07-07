package rs.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
@Document(indexName = "test")
public class Topic {
    @NonNull
    private String id;
}
