package rs.loader.model;

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
public class Comment implements Model {
    @NonNull
    private String id;
    @NonNull
    private String author;
    @NonNull
    private String parentId;
    @NonNull
    private String topic;
    @NonNull
    private String topicId;
    @NonNull
    private String linkId;
    @NonNull
    private String bodyHTML;
    @NonNull
    private String body;
    @NonNull
    private Date created;
    @NonNull
    private Long score;
}
