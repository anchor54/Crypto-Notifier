package subscriber;

import contract.ISubscriber;
import model.CryptoTopics;
import model.Event;
import model.PriceUpdate;
import service.EventBus;

import java.io.Serializable;
import java.math.BigDecimal;

public class PortfolioSubscriber implements ISubscriber {
    private final String name;
    private final BigDecimal[] holdings;  // Array of holdings in order [BTC, ETH, SOL, ADA, XRP]
    private final String[] symbols = {"BTC", "ETH", "SOL", "ADA", "XRP"};
    private final BigDecimal[] lastPrices;
    private BigDecimal portfolioValue;

    public PortfolioSubscriber(String name, BigDecimal[] holdings) {
        this.name = name;
        this.holdings = holdings;
        this.lastPrices = new BigDecimal[holdings.length];
        this.portfolioValue = BigDecimal.ZERO;

        // Subscribe to price updates for all cryptos
        EventBus.getInstance().subscribe(CryptoTopics.PRICE_UPDATES, this);
    }

    @Override
    public <T extends Serializable> void notify(Event<T> event) {
        if (event.data instanceof PriceUpdate update) {
            String symbol = update.getCrypto().symbol();

            // Find the index of this symbol
            int index = -1;
            for (int i = 0; i < symbols.length; i++) {
                if (symbols[i].equals(symbol)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                // Update the price for this crypto
                lastPrices[index] = update.getPrice();

                // Recalculate portfolio value
                BigDecimal newPortfolioValue = calculatePortfolioValue();

                // If we have a previous portfolio value, calculate the change
                if (portfolioValue.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal change = newPortfolioValue.subtract(portfolioValue);
                    BigDecimal percentChange = change
                            .divide(portfolioValue, 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(BigDecimal.valueOf(100));

                    // Only show significant changes (>0.5%)
                    if (percentChange.abs().compareTo(BigDecimal.valueOf(0.5)) > 0) {
                        String direction = change.compareTo(BigDecimal.ZERO) > 0 ? "increased" : "decreased";
                        System.out.printf("💼 %s PORTFOLIO: Value has %s by %.2f%% to $%.2f%n",
                                name, direction, percentChange.abs(), newPortfolioValue);
                    }
                }

                portfolioValue = newPortfolioValue;
            }
        }
    }

    private BigDecimal calculatePortfolioValue() {
        BigDecimal total = BigDecimal.ZERO;

        for (int i = 0; i < holdings.length; i++) {
            if (lastPrices[i] != null) {
                total = total.add(holdings[i].multiply(lastPrices[i]));
            }
        }

        return total;
    }
}