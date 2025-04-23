import contract.IPublishable;
import contract.IPublisher;

public class Publisher<T> implements IPublisher<T> {

    private final IPublishable<T> eventPublisher;

    public Publisher() {
        eventPublisher = EventBus.getInstance();
    }

    @Override
    public boolean publish(T data) {
        return false;
    }
}