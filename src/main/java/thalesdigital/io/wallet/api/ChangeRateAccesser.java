package thalesdigital.io.wallet.api;

import thalesdigital.io.wallet.domain.exchangerate.ChangeRate;

import java.util.Currency;

public interface ChangeRateAccesser {
  ChangeRate changeRate(Currency from, Currency to) throws NoChangeRateException;
}
