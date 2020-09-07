package thalesdigital.io.wallet.domain;

import java.math.BigDecimal;
import java.util.Currency;

public class Money {
  public Money(BigDecimal amount, Currency currency) {
    this.amount = amount;
    this.currency = currency;
  }

  public final Currency currency;
  public final BigDecimal amount;

  public Money applyRate(ChangeRate rate) {
    return new Money(amount.multiply(rate.getRate()), rate.to());
  }
  public Money plus(Money other) {
    return new Money(this.amount.add(other.amount), currency);
  }
}
