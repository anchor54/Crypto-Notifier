import contract.IPublishable;
import contract.IPublisher;
import model.Event;
import model.Topic;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public class Publisher implements IPublisher {
    private final Topic topic;
    private final IPublishable eventPublisher;

    public Publisher(final Topic topic) {
        this.topic = topic;
        eventPublisher = EventBus.getInstance();
    }

    @Override
    public <T extends Serializable> void publish(T data) {
        Event<T> event = new Event<>(data);
        CompletableFuture.runAsync(() -> eventPublisher.publish(topic, event));
    }
}