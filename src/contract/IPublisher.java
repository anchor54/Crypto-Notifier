package contract;

import model.Topic;

import java.io.Serializable;

public interface IPublisher {
    <T extends Serializable> void publish(Topic topic, T data);
}