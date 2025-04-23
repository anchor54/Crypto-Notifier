import publisher.MarketCapPublisher;
import publisher.PriceUpdatePublisher;
import publisher.TradePublisher;
import subscriber.PortfolioSubscriber;
import subscriber.PriceAlertSubscriber;
import subscriber.TradeWatcherSubscriber;
import subscriber.VolumeAlertSubscriber;

import java.math.BigDecimal;

public class CryptoNotifierDemo {
    public static void main(String[] args) {
        System.out.println("Starting Crypto Notifier System...");

        // Create publishers
        PriceUpdatePublisher pricePublisher = new PriceUpdatePublisher(5000);  // Price updates every 5 seconds
        MarketCapPublisher marketCapPublisher = new MarketCapPublisher(15000);  // Market cap updates every 15 seconds
        TradePublisher tradePublisher = new TradePublisher(2000, 8000);  // Trades every 2-8 seconds

        // Create subscribers

        // Price alert subscribers
        new PriceAlertSubscriber("Bitcoin Watcher", "BTC", new BigDecimal("0.5"));  // Alert on 0.5% change
        new PriceAlertSubscriber("Ethereum Watcher", "ETH", new BigDecimal("1.0"));  // Alert on 1% change
        new PriceAlertSubscriber("Solana Watcher", "SOL", new BigDecimal("2.0"));  // Alert on 2% change

        // Volume alert subscribers
        new VolumeAlertSubscriber("Bitcoin Volume Alert", "BTC", new BigDecimal("20"));  // Alert on 20M+ volume
        new VolumeAlertSubscriber("Ethereum Volume Alert", "ETH", new BigDecimal("15"));  // Alert on 15M+ volume

        // Trade watcher for large trades
        new TradeWatcherSubscriber("Whale Alert", new BigDecimal("10000"));  // Alert on trades >$10,000

        // Portfolio subscriber
        BigDecimal[] aliceHoldings = {
                new BigDecimal("0.5"),    // 0.5 BTC
                new BigDecimal("5"),      // 5 ETH
                new BigDecimal("50"),     // 50 SOL
                new BigDecimal("1000"),   // 1000 ADA
                new BigDecimal("2000")    // 2000 XRP
        };
        new PortfolioSubscriber("Alice's Portfolio", aliceHoldings);

        BigDecimal[] bobHoldings = {
                new BigDecimal("0.25"),   // 0.25 BTC
                new BigDecimal("10"),     // 10 ETH
                new BigDecimal("20"),     // 20 SOL
                new BigDecimal("5000"),   // 5000 ADA
                new BigDecimal("1000")    // 1000 XRP
        };
        new PortfolioSubscriber("Bob's Portfolio", bobHoldings);

        // Start publishers
        pricePublisher.start();
        marketCapPublisher.start();
        tradePublisher.start();

        System.out.println("Crypto Notifier System running! Press Ctrl+C to exit.");
    }
}