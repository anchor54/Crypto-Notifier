package contract;

import model.Topic;

public interface ISubscribeable {
    boolean subscribe(Topic topic, ISubscriber subscriber);
    boolean unSubscribe(Topic topic, ISubscriber subscriber);
}