package thalesdigital.io.wallet.api;

import java.util.Currency;

public class NoChangeRateException extends Throwable {
  public NoChangeRateException(Currency from, Currency to) {
    super("Cannot find change for " + from.getSymbol() + " -> " + to.getSymbol());
  }
}
