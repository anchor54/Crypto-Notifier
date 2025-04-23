import contract.ISubscribeable;
import contract.ISubscriber;
import model.Event;
import model.Topic;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class Subscriber implements ISubscriber {
    private final Topic topic;
    private final String name;

    public Subscriber(final Topic topic) {
        this.topic = topic;
        this.name = UUID.randomUUID().toString();
        ISubscribeable eventPublisher = EventBus.getInstance();
        eventPublisher.subscribe(topic, this);
    }

    public Subscriber(final Topic topic, String name) {
        this.topic = topic;
        this.name = name;
        ISubscribeable eventPublisher = EventBus.getInstance();
        eventPublisher.subscribe(topic, this);
    }

    @Override
    public <T extends Serializable> void notify(Event<T> event) {
        CompletableFuture.runAsync(() -> System.out.println("{ " + name + " } Notified on [" + topic + "]: " + event));
    }
}