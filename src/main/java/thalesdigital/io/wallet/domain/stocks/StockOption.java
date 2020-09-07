package thalesdigital.io.wallet.domain.stocks;

import thalesdigital.io.wallet.domain.Money;

public class StockOption implements Stock {
  @Override
  public Money amount() {
    Money total = amount;
    for (int i = 1; i != count; ++i)
      total = total.plus(amount);
    return total;
  }

  public StockOption(int count, Money amount) {
    this.count = count;
    this.amount = amount;
  }

  private final int count;
  private final Money amount;
}
