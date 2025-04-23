package contract;

import model.Topic;

public interface IPublishable<T> {
    void publish(Topic topic, T data);
}