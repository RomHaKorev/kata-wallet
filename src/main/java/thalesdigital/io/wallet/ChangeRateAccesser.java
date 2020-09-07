package thalesdigital.io.wallet;

import thalesdigital.io.wallet.domain.ChangeRate;

import java.util.Currency;

public interface ChangeRateAccesser {
  ChangeRate changeRate(Currency from, Currency to) throws NoChangeRateException;
}
