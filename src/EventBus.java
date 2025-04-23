import contract.IPublishable;
import contract.ISubscribeable;
import contract.ISubscriber;
import exception.TopicNotFoundException;
import model.Event;
import model.Topic;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class EventBus<T> implements IPublishable<T>, ISubscribeable<T> {
    private final Map<Topic, List<ISubscriber<T>>> topicSubscriberMap;
    private final Map<Topic, ReadWriteLock> topicLockMap;
    private static EventBus<?> instance = null;

    private EventBus() {
        this.topicSubscriberMap = new ConcurrentHashMap<>();
        this.topicLockMap = new ConcurrentHashMap<>();
    }

    public static synchronized <T> EventBus<T> getInstance() {
        if (instance == null) instance = new EventBus<>();
        return (EventBus<T>) instance;
    }

    private <U> U tryReadLock(Topic topic, Supplier<U> tryReadBlock) {
        Lock readLock = topicLockMap.computeIfAbsent(topic, __ -> new ReentrantReadWriteLock()).readLock();
        try {
            readLock.lock();
            return tryReadBlock.get();
        } finally {
            readLock.unlock();
        }
    }

    private <U> U tryWriteLock(Topic topic, Supplier<U> tryWriteBlock) {
        Lock writeLock = topicLockMap.computeIfAbsent(topic, __ -> new ReentrantReadWriteLock()).writeLock();
        try {
            writeLock.lock();
            return tryWriteBlock.get();
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void publish(Topic topic, T data) throws RuntimeException {
        if (topic == null || data == null) {
            throw new IllegalArgumentException("topic and data cannot be null");
        }

        tryReadLock(topic, () -> {
            if (!topicSubscriberMap.containsKey(topic)) {
                throw new TopicNotFoundException("Topic " + topic + "is not found!");
            }
            topicSubscriberMap
                    .computeIfAbsent(topic, __ -> new CopyOnWriteArrayList<>())
                    .forEach(subscriber -> {
                        CompletableFuture.supplyAsync(() -> subscriber.notify(data));
                    });
            return true;
        });
    }

    @Override
    public boolean subscribe(Topic topic, ISubscriber<T> subscriber) {
        if (topic == null) {
            throw new IllegalArgumentException("Topic should not be null");
        }

        return tryWriteLock(topic, () -> {
            CompletableFuture.supplyAsync(() -> topicSubscriberMap
                    .computeIfAbsent(topic, __ -> new CopyOnWriteArrayList<>())
                    .add(subscriber));
            return true;
        });
    }

    @Override
    public boolean unSubscribe(Topic topic, ISubscriber<T> subscriber) {
        if (topic == null) {
            throw new IllegalArgumentException("Topic should not be null");
        }

        return tryWriteLock(topic, () -> {
            CompletableFuture.supplyAsync(() -> topicSubscriberMap
                    .computeIfAbsent(topic, __ -> new CopyOnWriteArrayList<>())
                    .remove(subscriber));
            return true;
        });
    }
}
