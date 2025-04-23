package model;

/**
 * @param topicName topic name is expected to be unique;
 */
public record Topic(String topicName) {
    @Override
    public String toString() {
        return "topic-" + topicName;
    }
}