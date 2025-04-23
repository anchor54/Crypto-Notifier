package subscriber;

import contract.ISubscriber;
import model.CryptoTopics;
import model.Event;
import model.TradeUpdate;
import service.EventBus;

import java.io.Serializable;
import java.math.BigDecimal;

public class TradeWatcherSubscriber implements ISubscriber {
    private final String name;
    private final BigDecimal minTradeValue;  // Minimum USD value to report

    public TradeWatcherSubscriber(String name, BigDecimal minTradeValue) {
        this.name = name;
        this.minTradeValue = minTradeValue;

        // Subscribe to all trades
        EventBus.getInstance().subscribe(CryptoTopics.TRADE_UPDATES, this);
    }

    @Override
    public <T extends Serializable> void notify(Event<T> event) {
        if (event.data instanceof TradeUpdate) {
            TradeUpdate trade = (TradeUpdate) event.data;

            // Calculate trade value in USD
            BigDecimal tradeValue = trade.getAmount().multiply(trade.getPrice());

            // Check if trade value exceeds minimum
            if (tradeValue.compareTo(minTradeValue) > 0) {
                System.out.printf("💰 %s LARGE TRADE: %s %s of %s (%.6f coins) worth $%.2f%n",
                        name, trade.getType(),
                        trade.getCrypto().symbol(),
                        trade.getCrypto().name(),
                        trade.getAmount(),
                        tradeValue);
            }
        }
    }
}