package model;

public class Topic {
    // topic name is expected to be unique;
    public final String topicName;

    public Topic(String topicName) {
        this.topicName = topicName;
    }
    @Override
    public String toString() {
        return "topic-" + topicName;
    }
}
