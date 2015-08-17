package rs.loader.service.validator;

import org.springframework.stereotype.Component;
import rs.loader.model.Link;

@Component
public class LinkValidator implements Validator<Link>{

    @Override
    public boolean isValid(Link link) {
        return true;
    }
}
