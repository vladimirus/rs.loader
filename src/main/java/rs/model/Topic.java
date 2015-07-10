package rs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

@NoArgsConstructor //this needed for ES
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document(indexName = "test")
public class Topic implements Model {
    @NonNull
    private String id;
    @NonNull
    private String displayName;
    @NonNull
    private String title;
    @NonNull
    private Date created;
    @NonNull
    private Boolean nsfw;
    @NonNull
    private Long subscribers;
    @NonNull
    private String description;
    @NonNull
    private String type;
    @NonNull
    private Date updated;
}
