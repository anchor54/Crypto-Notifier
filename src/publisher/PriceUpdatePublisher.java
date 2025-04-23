package publisher;

import model.CryptoCurrency;
import model.CryptoTopics;
import model.PriceUpdate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class PriceUpdatePublisher extends CryptoPublisher {
    private final long updateIntervalMs;

    public PriceUpdatePublisher(long updateIntervalMs) {
        super();
        this.updateIntervalMs = updateIntervalMs;
    }

    @Override
    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                Map<String, CryptoCurrency> cryptos = dataService.getAllCryptoCurrencies();

                for (String symbol : cryptos.keySet()) {
                    PriceUpdate update = dataService.fetchPriceUpdate(symbol);

                    // Publish to general price topic
                    publish(CryptoTopics.PRICE_UPDATES, update);

                    // Publish to crypto-specific topics
                    publish(CryptoTopics.forCrypto(symbol), update);
                    publish(CryptoTopics.forCryptoPrice(symbol), update);
                }
            } catch (Exception e) {
                System.err.println("Error publishing price updates: " + e.getMessage());
            }
        }, 0, updateIntervalMs, TimeUnit.MILLISECONDS);
    }
}
