package contract;

public interface IPublisher<T> {
    boolean publish(T data);
}
