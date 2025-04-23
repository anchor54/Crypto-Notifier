package model;

import java.io.Serializable;

public record CryptoCurrency(String symbol, String name) implements Serializable {

    @Override
    public String toString() {
        return name + " (" + symbol + ")";
    }
}
