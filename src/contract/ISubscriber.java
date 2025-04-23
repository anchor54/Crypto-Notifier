package contract;

import model.Event;

import java.io.Serializable;

public interface ISubscriber {
    <T extends Serializable> void notify(Event<T> event);
}