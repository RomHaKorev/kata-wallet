package thalesdigital.io.wallet;

import org.junit.jupiter.api.Assertions;
import thalesdigital.io.wallet.domain.Money;
import thalesdigital.io.wallet.domain.Wallet;
import thalesdigital.io.wallet.domain.stocks.CheckingAccount;
import thalesdigital.io.wallet.helpers.WalletTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;

public class WalletTest {

  @Test
  @DisplayName("Should return 0 if wallet is empty")
  void test1() {
    given_an_empty_wallet()
        .when_I_consult_the_amount_in("EUR")
        .then_I_should_have(0D);
  }

  @Test
  @DisplayName("Should return the sum of all amount if in the same currency")
  void test2() {
    given_a_wallet_with(
        "A checking account with EUR 12\n" +
        "A checking account with EUR 38")
    .when_I_consult_the_amount_in("EUR")
    .then_I_should_have(50D);
  }

  @Test
  @DisplayName("Should return the sum of all amounts converted in another currency")
  void test3() {
    given_a_wallet_with(
            "A checking account with EUR 12\n" +
            "A checking account with EUR 38")
        .and_exchange_rates("EUR to USD -> 2")
        .when_I_consult_the_amount_in("USD")
        .then_I_should_have(100D);
  }

  @Test
  @DisplayName("Should return the sum of all amounts converted in the right currency")
  void test4() {
    given_a_wallet_with(
            "A checking account with GBP 10\n" +
            "A checking account with EUR 10")
        .and_exchange_rates("EUR to USD -> 2\n" +
                            "GBP to USD -> 3")
        .when_I_consult_the_amount_in("USD")
        .then_I_should_have(50D);
  }

  @Test
  @DisplayName("Should return the amount of a stock options multiplied by its count")
  void test5() {
    given_a_wallet_with(
        "Stock options in Petroleum with 10 barrels at EUR 15 each")
        .when_I_consult_the_amount_in("EUR")
        .then_I_should_have(150D);
  }

  @Test
  @DisplayName("Should return the amount of a stock options multiplied by its count")
  void test6() {
    given_a_wallet_with(
        "A checking account with GBP 10\n" +
        "Stock options in Petroleum with 10 barrels at USD 15 each")
        .and_exchange_rates("GBP to EUR -> 2\n" +
                            "USD to EUR -> 4")
        .when_I_consult_the_amount_in("EUR")
        .then_I_should_have(10D * 2 + 150D * 4);
  }

  @Test
  @DisplayName("Integration test: nominal")
  void test7() {
    ChangeRateCatalog catalog = new ChangeRateCatalog((from, to) -> "2");
    CurrencyConverter converter = new CurrencyConverter(catalog);
    Wallet w = new Wallet();

    w = w.with(new CheckingAccount(new Money(new BigDecimal(120.0), Currency.getInstance(Locale.FRANCE))));

    Assertions.assertEquals(new BigDecimal(240), w.evaluateIn((amount, to) -> {
      try {
        return converter.convertTo(amount, to);
      } catch (NoChangeRateException e) {
        return null;
      }
    }, Currency.getInstance(Locale.US)).amount);
  }


  @Test
  @DisplayName("Integration test: error")
  void test8() {
    ChangeRateCatalog catalog = new ChangeRateCatalog((from, to) -> {
      throw new RuntimeException();
    });
    CurrencyConverter converter = new CurrencyConverter(catalog);
    final Wallet w = new Wallet().with(new CheckingAccount(new Money(new BigDecimal(120.0), Currency.getInstance(Locale.FRANCE))));

    w.with(new CheckingAccount(new Money(new BigDecimal(120.0), Currency.getInstance(Locale.FRANCE))));

    Assertions.assertThrows(RuntimeException.class, () -> w.evaluateIn((amount, to) -> {
      try {
        return converter.convertTo(amount, to);
      } catch (NoChangeRateException e) {
        return null;
      }
    }, Currency.getInstance(Locale.US)));
  }

  WalletTestHelper given_an_empty_wallet() {
    return new WalletTestHelper("");
  }
  WalletTestHelper given_a_wallet_with(String stocks) {
    return new WalletTestHelper(stocks);
  }

}
