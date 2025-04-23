package subscriber;

import contract.ISubscriber;
import model.CryptoTopics;
import model.Event;
import model.MarketCapUpdate;
import service.EventBus;

import java.io.Serializable;
import java.math.BigDecimal;

public class VolumeAlertSubscriber implements ISubscriber {
    private final String name;
    private final String cryptoSymbol;
    private final BigDecimal volumeThreshold;  // In millions

    public VolumeAlertSubscriber(String name, String cryptoSymbol, BigDecimal volumeThreshold) {
        this.name = name;
        this.cryptoSymbol = cryptoSymbol;
        this.volumeThreshold = volumeThreshold;

        // Subscribe to the specific crypto market cap updates
        EventBus.getInstance().subscribe(CryptoTopics.forCryptoMarketCap(cryptoSymbol), this);
    }

    @Override
    public <T extends Serializable> void notify(Event<T> event) {
        if (event.data instanceof MarketCapUpdate) {
            MarketCapUpdate update = (MarketCapUpdate) event.data;

            // Check if the symbol matches
            if (!update.getCrypto().symbol().equals(cryptoSymbol)) {
                return;
            }

            // Check if volume exceeds threshold
            if (update.getVolume24h().compareTo(volumeThreshold) > 0) {
                System.out.printf("📊 %s VOLUME ALERT: %s 24h volume is $%.2f million, exceeding threshold of $%.2f million%n",
                        name, update.getCrypto().name(), update.getVolume24h(), volumeThreshold);
            }
        }
    }
}