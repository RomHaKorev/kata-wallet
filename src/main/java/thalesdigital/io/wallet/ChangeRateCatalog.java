package thalesdigital.io.wallet;

import thalesdigital.io.wallet.domain.ChangeRate;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.function.BiFunction;

public class ChangeRateCatalog implements ChangeRateAccesser {

  public ChangeRateCatalog(BiFunction<String, String, String> getter) {
    this.getter = getter;
  }

  @Override
  public ChangeRate changeRate(Currency from, Currency to) throws NoChangeRateException {
    try {
      if (from.equals(to)) {
        return new ChangeRate(new BigDecimal(1), from, to);
      }
      String rate = getter.apply(from.getSymbol(), to.getSymbol());
      return new ChangeRate(new BigDecimal(rate), from, to);
    } catch (Exception e) {
      throw new NoChangeRateException(from, to);
    }
  }


  private final BiFunction<String, String, String> getter;

}
