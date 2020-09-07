package thalesdigital.io.wallet.helpers;

import org.junit.jupiter.api.Assertions;
import thalesdigital.io.wallet.domain.exchangerate.CurrencyConverter;
import thalesdigital.io.wallet.api.NoChangeRateException;
import thalesdigital.io.wallet.domain.Money;
import thalesdigital.io.wallet.domain.Wallet;

import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

public class WalletTestHelper {
  public WalletTestHelper(String stocks) {
    StockParser parser = new StockParser();
    parser.parseStocks(stocks).forEach(
        it -> sut = sut.with(it)
    );
  }



  public WalletTestHelper and_exchange_rates(String rates) {
    stub.applyRates(rates);
    return this;
  }

  public WalletTestHelper when_I_consult_the_amount_in(String currency) {
    result = sut.evaluate((amount) -> {
      try {
        return converter.convertTo(amount, Currency.getInstance(currency));
      } catch (NoChangeRateException e) {
        e.printStackTrace();
      }
      return null;
    }).collect(Collectors.toList());
    return this;
  }

  public void then_I_should_see_nothing() {
    Assertions.assertEquals(0, result.size());
  }

  public WalletTestHelper then_I_should_have(Double... expected) {
    Assertions.assertEquals(Arrays.asList(expected),
        result.stream().map(it -> it.amount.doubleValue()).collect(Collectors.toList()));
    return this;
  }

  List<Money> result;
  ChangeRateCatalogStub stub = new ChangeRateCatalogStub();
  CurrencyConverter converter = new CurrencyConverter(stub);
  Wallet sut = new Wallet();


}
