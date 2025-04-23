package contract;

import model.Topic;

public interface ISubscriber<T> {
    boolean notify(T data);
}
