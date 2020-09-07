package thalesdigital.io.wallet;

import thalesdigital.io.wallet.domain.ChangeRate;
import thalesdigital.io.wallet.domain.Money;

import java.util.Currency;

public class CurrencyConverter {
  public CurrencyConverter(ChangeRateAccesser changeRateCatalog) {
    this.changeRateCatalog = changeRateCatalog;
  }

  private final ChangeRateAccesser changeRateCatalog;

  public Money convertTo(Money amount, Currency to) throws NoChangeRateException {
      ChangeRate rate = changeRateCatalog.changeRate(amount.currency, to);
      return amount.applyRate(rate);
  }
}
