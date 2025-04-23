package contract;

import java.io.Serializable;

public interface IPublisher {
    <T extends Serializable> void publish(T data);
}