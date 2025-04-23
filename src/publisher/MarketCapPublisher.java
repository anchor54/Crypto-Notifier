package publisher;

import model.CryptoCurrency;
import model.CryptoTopics;
import model.MarketCapUpdate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MarketCapPublisher extends CryptoPublisher {
    private final long updateIntervalMs;

    public MarketCapPublisher(long updateIntervalMs) {
        super();
        this.updateIntervalMs = updateIntervalMs;
    }

    @Override
    public void start() {
        scheduler.scheduleAtFixedRate(() -> {
            try {
                Map<String, CryptoCurrency> cryptos = dataService.getAllCryptoCurrencies();

                for (String symbol : cryptos.keySet()) {
                    MarketCapUpdate update = dataService.fetchMarketCapUpdate(symbol);

                    // Publish to general market cap topic
                    publish(CryptoTopics.MARKET_CAP_UPDATES, update);

                    // Publish to crypto-specific topics
                    publish(CryptoTopics.forCrypto(symbol), update);
                    publish(CryptoTopics.forCryptoMarketCap(symbol), update);
                }
            } catch (Exception e) {
                System.err.println("Error publishing market cap updates: " + e.getMessage());
            }
        }, 0, updateIntervalMs, TimeUnit.MILLISECONDS);
    }
}
