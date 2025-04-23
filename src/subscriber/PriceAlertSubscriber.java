package subscriber;

import contract.ISubscriber;
import model.CryptoTopics;
import model.Event;
import model.PriceUpdate;
import service.EventBus;

import java.io.Serializable;
import java.math.BigDecimal;

public class PriceAlertSubscriber implements ISubscriber {
    private final String name;
    private final String cryptoSymbol;
    private final BigDecimal alertThreshold;
    private BigDecimal lastNotifiedPrice;

    public PriceAlertSubscriber(String name, String cryptoSymbol, BigDecimal alertThreshold) {
        this.name = name;
        this.cryptoSymbol = cryptoSymbol;
        this.alertThreshold = alertThreshold;
        this.lastNotifiedPrice = null;

        // Subscribe to the specific crypto price updates
        EventBus.getInstance().subscribe(CryptoTopics.forCryptoPrice(cryptoSymbol), this);
    }

    @Override
    public <T extends Serializable> void notify(Event<T> event) {
        if (event.data instanceof PriceUpdate) {
            PriceUpdate update = (PriceUpdate) event.data;

            // Check if the symbol matches
            if (!update.getCrypto().symbol().equals(cryptoSymbol)) {
                return;
            }

            BigDecimal currentPrice = update.getPrice();

            // If this is the first update, record the price and exit
            if (lastNotifiedPrice == null) {
                lastNotifiedPrice = currentPrice;
                return;
            }

            // Calculate percentage change since last notification
            BigDecimal change = currentPrice
                    .subtract(lastNotifiedPrice)
                    .divide(lastNotifiedPrice, 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(BigDecimal.valueOf(100))
                    .abs();

            // If change exceeds threshold, notify
            if (change.compareTo(alertThreshold) > 0) {
                BigDecimal changeValue = currentPrice.subtract(lastNotifiedPrice);
                String direction = changeValue.compareTo(BigDecimal.ZERO) > 0 ? "increased" : "decreased";

                System.out.printf("🚨 %s ALERT: %s price has %s by %.2f%% (%.2f → %.2f)%n",
                        name, update.getCrypto().name(), direction, change, lastNotifiedPrice, currentPrice);

                // Update last notified price
                lastNotifiedPrice = currentPrice;
            }
        }
    }
}