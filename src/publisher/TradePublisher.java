package publisher;

import model.CryptoCurrency;
import model.CryptoTopics;
import model.TradeUpdate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class TradePublisher extends CryptoPublisher {
    private final long minIntervalMs;
    private final long maxIntervalMs;

    public TradePublisher(long minIntervalMs, long maxIntervalMs) {
        super();
        this.minIntervalMs = minIntervalMs;
        this.maxIntervalMs = maxIntervalMs;
    }

    @Override
    public void start() {
        scheduleTrade();
    }

    private void scheduleTrade() {
        // Schedule the next trade with a random delay
        long delay = minIntervalMs + (long)(random.nextDouble() * (maxIntervalMs - minIntervalMs));

        scheduler.schedule(() -> {
            try {
                Map<String, CryptoCurrency> cryptos = dataService.getAllCryptoCurrencies();

                // Choose a random crypto
                String[] symbols = cryptos.keySet().toArray(new String[0]);
                String randomSymbol = symbols[random.nextInt(symbols.length)];

                // Generate a random trade
                TradeUpdate trade = dataService.generateRandomTrade(randomSymbol);

                // Publish to general trade topic
                publish(CryptoTopics.TRADE_UPDATES, trade);

                // Publish to crypto-specific topics
                publish(CryptoTopics.forCrypto(randomSymbol), trade);
                publish(CryptoTopics.forCryptoTrades(randomSymbol), trade);

                // Schedule the next trade
                scheduleTrade();
            } catch (Exception e) {
                System.err.println("Error publishing trade: " + e.getMessage());
                // Try again later
                scheduleTrade();
            }
        }, delay, TimeUnit.MILLISECONDS);
    }
}