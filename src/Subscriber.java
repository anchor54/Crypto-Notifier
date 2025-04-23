import contract.ISubscribeable;
import contract.ISubscriber;
import model.Event;
import model.Topic;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;

public class Subscriber implements ISubscriber {
    private final Topic topic;
    private final ISubscribeable eventPublisher;

    public Subscriber(final Topic topic) {
        this.topic = topic;
        eventPublisher = EventBus.getInstance();
    }

    @Override
    public <T extends Serializable> void notify(Event<T> event) {
        CompletableFuture.runAsync(() -> System.out.println("Notified: " + event));
    }
}