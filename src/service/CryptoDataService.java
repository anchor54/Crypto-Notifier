package service;

import model.CryptoCurrency;
import model.PriceUpdate;
import model.MarketCapUpdate;
import model.TradeUpdate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class CryptoDataService {
    private static CryptoDataService instance;
    private final Map<String, CryptoCurrency> cryptoCurrencies;
    private final Map<String, BigDecimal> lastPrices;
    private final Map<String, BigDecimal> marketCaps;
    private final Random random;

    private CryptoDataService() {
        cryptoCurrencies = new HashMap<>();
        lastPrices = new ConcurrentHashMap<>();
        marketCaps = new ConcurrentHashMap<>();
        random = new Random();

        // Initialize with some sample data
        initializeData();
    }

    public static synchronized CryptoDataService getInstance() {
        if (instance == null) {
            instance = new CryptoDataService();
        }
        return instance;
    }

    private void initializeData() {
        // Add some cryptocurrencies
        addCrypto("BTC", "Bitcoin", "45000.00", "850");
        addCrypto("ETH", "Ethereum", "3200.00", "380");
        addCrypto("SOL", "Solana", "140.00", "55");
        addCrypto("ADA", "Cardano", "0.45", "16");
        addCrypto("XRP", "Ripple", "0.52", "28");
    }

    private void addCrypto(String symbol, String name, String initialPrice, String marketCapInBillions) {
        CryptoCurrency crypto = new CryptoCurrency(symbol, name);
        cryptoCurrencies.put(symbol, crypto);
        lastPrices.put(symbol, new BigDecimal(initialPrice));
        marketCaps.put(symbol, new BigDecimal(marketCapInBillions));
    }

    public PriceUpdate fetchPriceUpdate(String symbol) {
        if (!cryptoCurrencies.containsKey(symbol)) {
            throw new IllegalArgumentException("Unknown cryptocurrency: " + symbol);
        }

        CryptoCurrency crypto = cryptoCurrencies.get(symbol);
        BigDecimal lastPrice = lastPrices.get(symbol);

        // Generate a random price change (-3% to +3%)
        double changePercent = (random.nextDouble() * 6) - 3; // -3% to +3%
        BigDecimal percentChange = BigDecimal.valueOf(changePercent)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal priceChange = lastPrice
                .multiply(percentChange)
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal newPrice = lastPrice.add(priceChange).setScale(2, RoundingMode.HALF_UP);

        // Update the last price for next time
        lastPrices.put(symbol, newPrice);

        return new PriceUpdate(crypto, newPrice, priceChange, percentChange);
    }

    public MarketCapUpdate fetchMarketCapUpdate(String symbol) {
        if (!cryptoCurrencies.containsKey(symbol)) {
            throw new IllegalArgumentException("Unknown cryptocurrency: " + symbol);
        }

        CryptoCurrency crypto = cryptoCurrencies.get(symbol);
        BigDecimal marketCap = marketCaps.get(symbol);

        // Generate a random 24h volume (between 3% and 8% of market cap)
        double volumePercent = 3 + (random.nextDouble() * 5); // 3% to 8%
        BigDecimal volume24h = marketCap
                .multiply(BigDecimal.valueOf(volumePercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        // Slightly adjust market cap based on price changes (-2% to +2%)
        double capChangePercent = (random.nextDouble() * 4) - 2; // -2% to +2%
        BigDecimal capChange = marketCap
                .multiply(BigDecimal.valueOf(capChangePercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        marketCap = marketCap.add(capChange).setScale(2, RoundingMode.HALF_UP);
        marketCaps.put(symbol, marketCap);

        return new MarketCapUpdate(crypto, marketCap, volume24h);
    }

    public TradeUpdate generateRandomTrade(String symbol) {
        if (!cryptoCurrencies.containsKey(symbol)) {
            throw new IllegalArgumentException("Unknown cryptocurrency: " + symbol);
        }

        CryptoCurrency crypto = cryptoCurrencies.get(symbol);
        BigDecimal lastPrice = lastPrices.get(symbol);

        // Generate a random price variation for this trade
        double priceVariation = (random.nextDouble() * 0.5) - 0.25; // -0.25% to +0.25%
        BigDecimal tradePrice = lastPrice
                .multiply(BigDecimal.ONE.add(BigDecimal.valueOf(priceVariation).divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP)))
                .setScale(2, RoundingMode.HALF_UP);

        // Generate a random amount
        double amount = 0;
        // Amount depends on the coin (e.g., more BTC is worth more than the same amount of XRP)
        if (symbol.equals("BTC")) {
            amount = 0.01 + (random.nextDouble() * 0.5); // 0.01 - 0.51 BTC
        } else if (symbol.equals("ETH")) {
            amount = 0.1 + (random.nextDouble() * 2); // 0.1 - 2.1 ETH
        } else {
            amount = 10 + (random.nextDouble() * 200); // 10 - 210 units
        }

        BigDecimal tradeAmount = BigDecimal.valueOf(amount).setScale(6, RoundingMode.HALF_UP);

        // Buy or sell
        String type = random.nextBoolean() ? "BUY" : "SELL";

        return new TradeUpdate(crypto, tradeAmount, tradePrice, type);
    }

    public Map<String, CryptoCurrency> getAllCryptoCurrencies() {
        return new HashMap<>(cryptoCurrencies);
    }
}
