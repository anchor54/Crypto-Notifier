package model;

public class CryptoTopics {
    public static final Topic PRICE_UPDATES = new Topic("crypto.price");
    public static final Topic MARKET_CAP_UPDATES = new Topic("crypto.marketcap");
    public static final Topic TRADE_UPDATES = new Topic("crypto.trades");

    // Topics for specific cryptocurrencies
    public static Topic forCrypto(String symbol) {
        return new Topic("crypto." + symbol.toLowerCase());
    }

    public static Topic forCryptoPrice(String symbol) {
        return new Topic("crypto." + symbol.toLowerCase() + ".price");
    }

    public static Topic forCryptoMarketCap(String symbol) {
        return new Topic("crypto." + symbol.toLowerCase() + ".marketcap");
    }

    public static Topic forCryptoTrades(String symbol) {
        return new Topic("crypto." + symbol.toLowerCase() + ".trades");
    }
}