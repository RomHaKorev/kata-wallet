package thalesdigital.io.wallet.domain;

import thalesdigital.io.wallet.domain.stocks.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

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

  public Stream<Money> evaluate(Function<Money, Money> converter) {
    return stocks.stream()
        .map(s ->converter.apply(s.amount()));
  }

  private final List<Stock> stocks;
}
