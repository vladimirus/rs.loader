package rs.loader.service.convert;

import org.springframework.stereotype.Component;
import rs.loader.model.Comment;

import java.util.Date;

@Component
public class CommentConverter implements Converter<com.github.jreddit.entity.Comment, Comment> {

    @Override
    public Comment convert(com.github.jreddit.entity.Comment from) {
        return Comment.builder()
                .id(from.getFullName())
                .author(from.getAuthor())
                .parentId(from.getParentId())
                .topic(from.getSubreddit())
                .topicId(from.getSubredditId())
                .linkId(from.getLinkId())
                .bodyHTML(from.getBodyHTML())
                .body(from.getBody())
                .created(new Date((long) from.getCreatedUTC() * 1000))
                .score((long) from.getScore())
                .build();
    }
}
