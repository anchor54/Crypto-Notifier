package publisher;

import contract.IPublishable;
import contract.IPublisher;
import model.Event;
import model.Topic;
import service.CryptoDataService;
import service.EventBus;

import java.io.Serializable;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public abstract class CryptoPublisher implements IPublisher {
    protected final IPublishable eventBus;
    protected final CryptoDataService dataService;
    protected final ScheduledExecutorService scheduler;
    protected final Random random;

    public CryptoPublisher() {
        this.eventBus = EventBus.getInstance();
        this.dataService = CryptoDataService.getInstance();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.random = new Random();
    }

    @Override
    public <T extends Serializable> void publish(Topic topic, T data) {
        Event<T> event = new Event<>(data);
        eventBus.publish(topic, event);
    }

    public abstract void start();

    public void stop() {
        scheduler.shutdown();
    }
}
