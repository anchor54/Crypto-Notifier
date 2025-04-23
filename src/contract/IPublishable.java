package contract;

import model.Event;
import model.Topic;

import java.io.Serializable;

public interface IPublishable {
    <T extends Serializable> void publish(Topic topic, Event<T> event);
}