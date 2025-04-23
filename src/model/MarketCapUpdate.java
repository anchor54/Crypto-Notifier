package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MarketCapUpdate implements Serializable {
    private final CryptoCurrency crypto;
    private final BigDecimal marketCap;
    private final BigDecimal volume24h;
    private final LocalDateTime timestamp;

    public MarketCapUpdate(CryptoCurrency crypto, BigDecimal marketCap, BigDecimal volume24h) {
        this.crypto = crypto;
        this.marketCap = marketCap;
        this.volume24h = volume24h;
        this.timestamp = LocalDateTime.now();
    }

    public CryptoCurrency getCrypto() {
        return crypto;
    }

    public BigDecimal getMarketCap() {
        return marketCap;
    }

    public BigDecimal getVolume24h() {
        return volume24h;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s: Market Cap: $%s B, 24h Volume: $%s M",
                crypto,
                marketCap,
                volume24h);
    }
}
