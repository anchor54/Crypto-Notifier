import contract.IPublisher;
import model.Topic;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CryptoNotifierDemo {
    public static void main(String[] args) {
        Topic topic_BTC = new Topic("BTC");
        Topic topic_DogeCoin = new Topic("Doge Coin");

        List<IPublisher> publishers = List.of(new Publisher(topic_BTC), new Publisher(topic_DogeCoin));

        for (int i = 1; i <= 4; i++) {
            new Subscriber(topic_BTC, "BTC-Subscriber-" + i);
        }
        for (int i = 1; i <= 6; i++) {
            new Subscriber(topic_DogeCoin, "Dodge-Subscriber-" + i);
        }

        Executors.newScheduledThreadPool(2).scheduleAtFixedRate(() -> {
            publishers.get((int)(Math.random() * 2)).publish("Publishing coin price");
        }, 0, (long) (Math.random() * 100 + 1), TimeUnit.MILLISECONDS) ;
    }
}