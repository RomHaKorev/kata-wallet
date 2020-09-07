package thalesdigital.io.wallet;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import thalesdigital.io.wallet.helpers.ConvertTestHelper;


public class ConverterTest {
  @Test
  @DisplayName("Can convert euro to USD")
  void test1() {
    given_the_exchange_rates("EUR to USD -> 2.5")
        .when_I_convert("EUR 2.0").to("USD")
        .then_amount_should_be("USD 5.0");
  }

  ConvertTestHelper given_the_exchange_rates(String rates) {
    return new ConvertTestHelper(rates);
  }
}
