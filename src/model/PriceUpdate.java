package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PriceUpdate implements Serializable {
    private final CryptoCurrency crypto;
    private final BigDecimal price;
    private final BigDecimal priceChange;
    private final BigDecimal percentChange;
    private final LocalDateTime timestamp;

    public PriceUpdate(CryptoCurrency crypto, BigDecimal price, BigDecimal priceChange,
                       BigDecimal percentChange) {
        this.crypto = crypto;
        this.price = price;
        this.priceChange = priceChange;
        this.percentChange = percentChange;
        this.timestamp = LocalDateTime.now();
    }

    public CryptoCurrency getCrypto() {
        return crypto;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getPriceChange() {
        return priceChange;
    }

    public BigDecimal getPercentChange() {
        return percentChange;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s: $%s (%s%% %s)",
                crypto,
                price,
                percentChange.abs(),
                priceChange.compareTo(BigDecimal.ZERO) >= 0 ? "↑" : "↓");
    }
}
