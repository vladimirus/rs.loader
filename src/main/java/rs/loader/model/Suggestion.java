package rs.loader.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

@NoArgsConstructor //this needed for ES
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document(indexName = "test")
public class Suggestion implements Model {
    @NonNull
    private String id;
    @NonNull
    private String original;
    @NonNull
    private String suggest;
}
