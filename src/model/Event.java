package model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Event<T extends Serializable> {
    public final String id;
    public final T data;
    public final LocalDateTime publishTimeStamp;

    public Event(T data) {
        this.id = UUID.randomUUID().toString();
        this.data = data;
        this.publishTimeStamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return publishTimeStamp + "event-" + id + ":" + data;
    }
}