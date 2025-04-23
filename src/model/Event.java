package model;

import java.time.LocalDateTime;

public class Event<T> {
    public final T data;
    public final LocalDateTime publishTimeStamp;

    public Event(T data) {
        this.data = data;
        this.publishTimeStamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "event-";
    }
}