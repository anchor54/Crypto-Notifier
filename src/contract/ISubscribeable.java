package contract;

import model.Topic;

public interface ISubscribeable<T> {
    boolean subscribe(Topic topic, ISubscriber<T> subscriber);
    boolean unSubscribe(Topic topic, ISubscriber<T> subscriber);
}
