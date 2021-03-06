package rs.loader.model;


import com.google.common.base.Splitter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

@NoArgsConstructor //this needed for ES
@AllArgsConstructor
@Builder
@Getter
@ToString
@EqualsAndHashCode
@Document(indexName = "test")
public class Link implements Model {
    @NonNull
    private String id;
    @NonNull
    private String title;
    @NonNull
    private String url;
    @NonNull
    private String commentsUrl;
    @NonNull
    private String author;
    @NonNull
    private String topic;
    @NonNull
    private String topicId;
    @NonNull
    private Long commentCount;
    @NonNull
    private Long score;
    @NonNull
    private Date created;
    @NonNull
    private Boolean self;
    @NonNull
    private Boolean nsfw;
    @NonNull
    private Boolean hidden;

    private String thumbnail;
    private String selfText;
    private String selfTextHtml;
    @Setter
    private String domain;
    @Setter
    private Collection<Comment> comments = new ArrayList<>();
    @Setter
    private String commentsBody;

    public String getIdWithoutType() {
        return Splitter.on("_")
                .trimResults()
                .splitToList(id)
                .stream()
                .skip(1)
                .findAny().orElse(id);
    }
}
