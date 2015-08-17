package rs.loader.service.validator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rs.loader.model.Topic;

@Component
public class TopicValidator implements Validator<Topic>{
    @Value("${rs.loader.topic.subscribers.min:-10}")
    long minNumberOfSubscribers;
    @Value("${rs.loader.topic.subscribers.max:100000000}")
    long maxNumberOfSubscribers;

    @Override
    public boolean isValid(Topic topic) {
        return topic.getSubscribers() >= minNumberOfSubscribers && topic.getSubscribers() <= maxNumberOfSubscribers;
    }
}
