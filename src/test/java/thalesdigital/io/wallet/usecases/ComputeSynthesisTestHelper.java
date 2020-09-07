package thalesdigital.io.wallet.usecases;

import org.junit.jupiter.api.Assertions;
import thalesdigital.io.wallet.domain.exchangerate.CurrencyConverter;
import thalesdigital.io.wallet.api.NoChangeRateException;
import thalesdigital.io.wallet.domain.Money;
import thalesdigital.io.wallet.domain.Wallet;
import thalesdigital.io.wallet.domain.usecases.ComputeSynthesis;
import thalesdigital.io.wallet.helpers.ChangeRateCatalogStub;
import thalesdigital.io.wallet.helpers.StockParser;

import java.math.BigDecimal;
import java.util.Currency;

class ComputeSynthesisTestHelper {
  public ComputeSynthesisTestHelper() {
    Wallet wallet = new Wallet();
    this.stub = new ChangeRateCatalogStub();
    this.converter = new CurrencyConverter(stub);
    this.sy = new ComputeSynthesis(wallet, (src, to) -> {
      try {
        return converter.convertTo(src, to);
      } catch (NoChangeRateException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public ComputeSynthesisTestHelper(String stocks) {
    StockParser parser = new StockParser();

    parser.parseStocks(stocks).forEach(
        it -> wallet = wallet.with(it)
    );
    this.stub = new ChangeRateCatalogStub();
    this.converter = new CurrencyConverter(stub);
    this.sy = new ComputeSynthesis(wallet, (src, to) -> {
      try {
        return converter.convertTo(src, to);
      } catch (NoChangeRateException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public ComputeSynthesisTestHelper when_I_summarize_in(String currency) {
    result = sy.evaluate(Currency.getInstance(currency));
    return this;
  }

  public ComputeSynthesisTestHelper then_I_should_see(String expectedStr) {
    Money expected = amountFromString(expectedStr);
    Assertions.assertEquals(expected.amount.setScale(2, BigDecimal.ROUND_CEILING), result.amount.setScale(2, BigDecimal.ROUND_CEILING));
    Assertions.assertEquals(expected.currency, result.currency);
    return this;
  }

  private Money amountFromString(String s) {
    return new Money(new BigDecimal(Double.valueOf(s.split(" ")[1])), Currency.getInstance(s.split(" ")[0]));
  }

  private Money result = null;
  private ChangeRateCatalogStub stub;
  private CurrencyConverter converter;
  private ComputeSynthesis sy;
  Wallet wallet = new Wallet();

  public ComputeSynthesisTestHelper and_exchange_rates(String s) {
    stub.applyRates(s);
    return this;
  }
}
