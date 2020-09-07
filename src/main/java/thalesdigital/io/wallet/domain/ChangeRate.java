package thalesdigital.io.wallet.domain;

import java.math.BigDecimal;
import java.util.Currency;

public class ChangeRate {
  private final BigDecimal rate;
  private final Currency from;
  private final Currency to;

  public ChangeRate(BigDecimal rate, Currency from, Currency to) {
    this.rate = rate;
    this.from = from;
    this.to = to;
  }

  public BigDecimal getRate() {
    return rate;
  }

  public Currency to() {
    return to;
  }

  public Currency from() {
    return from;
  }
}
