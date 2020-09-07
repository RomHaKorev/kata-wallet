package thalesdigital.io.wallet.usecases;

import org.junit.jupiter.api.Test;

public class ComputeSynthesisTest {

  @Test
  void should_return_0_if_wallet_empty() {
    given_a_wallet()
        .when_I_summarize_in("EUR")
        .then_I_should_see("EUR 0");
  }

  @Test
  void should_summarize_all_stock() {
    given_a_wallet_with(
                            "A checking account with GBP 10\n" +
                            "A checking account with EUR 10")
        .and_exchange_rates("EUR to USD -> 2\n" +
                            "GBP to USD -> 3")
        .when_I_summarize_in("USD")
        .then_I_should_see("USD 50");
  }

  ComputeSynthesisTestHelper given_a_wallet() {
    return new ComputeSynthesisTestHelper();
  }

  ComputeSynthesisTestHelper given_a_wallet_with(String stocks) {
    return new ComputeSynthesisTestHelper(stocks);
  }

}
