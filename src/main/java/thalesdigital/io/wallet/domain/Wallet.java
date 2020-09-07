package thalesdigital.io.wallet.domain;

import thalesdigital.io.wallet.domain.stocks.Stock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.function.BiFunction;

public class Wallet {
  public Wallet() {
    stocks = new ArrayList<>();
  }

  private Wallet(List<Stock> stocks) {
    this.stocks = stocks;
  }

  public Wallet with(Stock stock) {
    List<Stock> s = new ArrayList<>(stocks);
    s.add(stock);
    return new Wallet(s);
  }

  public Money evaluateIn(BiFunction<Money, Currency, Money> converter, Currency to) {
    return stocks.stream()
        .map(s ->converter.apply(s.amount(), to))
        .reduce(Money::plus)
        .orElse(new Money(new BigDecimal(0D), to));
  }

  private final List<Stock> stocks;
}
