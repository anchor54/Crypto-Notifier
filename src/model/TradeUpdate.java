package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TradeUpdate implements Serializable {
    private final CryptoCurrency crypto;
    private final String tradeId;
    private final BigDecimal amount;
    private final BigDecimal price;
    private final String type; // BUY or SELL
    private final LocalDateTime timestamp;

    public TradeUpdate(CryptoCurrency crypto, BigDecimal amount, BigDecimal price, String type) {
        this.crypto = crypto;
        this.tradeId = UUID.randomUUID().toString().substring(0, 8);
        this.amount = amount;
        this.price = price;
        this.type = type;
        this.timestamp = LocalDateTime.now();
    }

    public CryptoCurrency getCrypto() {
        return crypto;
    }

    public String getTradeId() {
        return tradeId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getType() {
        return type;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s: Trade #%s - %s %.6f coins at $%s",
                crypto,
                tradeId,
                type,
                amount,
                price);
    }
}
