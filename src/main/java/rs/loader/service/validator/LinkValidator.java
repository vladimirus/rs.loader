package rs.loader.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.loader.model.Link;

@Component
public class LinkValidator implements Validator<Link>{
    @Value("${rs.loader.link.score.min:-10}")
    long minScore;
    @Value("${rs.loader.link.score.max:100000000}")
    long maxScore;

    @Override
    public boolean isValid(Link link) {
        return link.getScore() >= minScore && link.getScore() <= maxScore;
    }
}
