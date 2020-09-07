package thalesdigital.io.wallet.domain.usecases;

import thalesdigital.io.wallet.domain.Money;
import thalesdigital.io.wallet.domain.Wallet;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.function.BiFunction;
import java.util.function.Function;

public class ComputeSynthesis {

  public ComputeSynthesis(Wallet wallet, BiFunction<Money, Currency, Money> currencyConverter) {
    this.wallet = wallet;
    this.currencyConverter = currencyConverter;
  }

  public Money evaluate(Currency to) {
    return wallet.evaluate((src) -> currencyConverter.apply(src, to))
        .reduce(Money::plus)
        .orElse(new Money(new BigDecimal(0D), to));
  }
  private Wallet wallet;
  private BiFunction<Money, Currency, Money> currencyConverter;
}
