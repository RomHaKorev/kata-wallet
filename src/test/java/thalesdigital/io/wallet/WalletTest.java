package thalesdigital.io.wallet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import thalesdigital.io.wallet.helpers.WalletTestHelper;

public class WalletTest {

  @Test
  @DisplayName("Should return 0 if wallet is empty")
  void test1() {
    given_an_empty_wallet()
        .when_I_consult_the_amount_in("EUR")
        .then_I_should_see_nothing();
  }

  @Test
  @DisplayName("Should return the sum of all amount if in the same currency")
  void test2() {
    given_a_wallet_with(
        "A checking account with EUR 12\n" +
        "A checking account with EUR 38")
    .when_I_consult_the_amount_in("EUR")
    .then_I_should_have(12D, 38D);
  }

  @Test
  @DisplayName("Should return the sum of all amounts converted in another currency")
  void test3() {
    given_a_wallet_with(
            "A checking account with EUR 12\n" +
            "A checking account with EUR 38")
        .and_exchange_rates("EUR to USD -> 2")
        .when_I_consult_the_amount_in("USD")
        .then_I_should_have(24D, 76D);
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
        .then_I_should_have(30D, 20D);
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
        .then_I_should_have(10D * 2, 150D * 4);
  }

  private WalletTestHelper given_an_empty_wallet() {
    return new WalletTestHelper("");
  }
  private WalletTestHelper given_a_wallet_with(String stocks) {
    return new WalletTestHelper(stocks);
  }

}
