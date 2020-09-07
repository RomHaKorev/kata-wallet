package thalesdigital.io.wallet.helpers;

import org.junit.jupiter.api.Assertions;
import thalesdigital.io.wallet.domain.Money;
import thalesdigital.io.wallet.domain.exchangerate.CurrencyConverter;
import thalesdigital.io.wallet.api.NoChangeRateException;

import java.math.BigDecimal;
import java.util.Currency;

public class ConvertTestHelper {
  ChangeRateCatalogStub stub = new ChangeRateCatalogStub();
  CurrencyConverter sut = new CurrencyConverter(stub);


  protected CurrencyConverter converter() {
    return sut;
  }

  public ConvertTestHelper(String rates) {
    stub.applyRates(rates);
  }



  private Money amountFromString(String s) {
    return new Money(new BigDecimal(Double.valueOf(s.split(" ")[1])), Currency.getInstance(s.split(" ")[0]));
  }

  public ConvertTestHelper when_I_convert(String from) {
    amount = amountFromString(from);
    return this;
  }

  public ConvertTestHelper to(String destination) {
    try {
      amount = sut.convertTo(amount, Currency.getInstance(destination));
    } catch (NoChangeRateException e) {
      raised = e;
    }
    return this;
  }

  public ConvertTestHelper then_amount_should_be(String expectedStr) {
    Money expected = amountFromString(expectedStr);
    Assertions.assertEquals(expected.amount.setScale(2), amount.amount.setScale(2));
    Assertions.assertEquals(expected.currency, amount.currency);
    return this;
  }

  private Money amount;
  private Throwable raised = null;
}
