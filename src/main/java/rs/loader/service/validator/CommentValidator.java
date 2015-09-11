package rs.loader.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.loader.model.Comment;

@Component
public class CommentValidator implements Validator<Comment>{
    @Value("${rs.loader.comment.score.min:-10}")
    long minScore;
    @Value("${rs.loader.comment.score.max:100000000}")
    long maxScore;

    @Override
    public boolean isValid(Comment comment) {
        return comment.getScore() >= minScore && comment.getScore() <= maxScore;
    }
}
