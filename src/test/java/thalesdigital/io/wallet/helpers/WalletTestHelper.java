package thalesdigital.io.wallet.helpers;

import org.junit.jupiter.api.Assertions;
import thalesdigital.io.wallet.*;
import thalesdigital.io.wallet.domain.Money;
import thalesdigital.io.wallet.domain.Wallet;
import thalesdigital.io.wallet.domain.stocks.CheckingAccount;
import thalesdigital.io.wallet.domain.stocks.StockOption;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WalletTestHelper {
  public WalletTestHelper(String stocks) {
    parseStocks(stocks);
  }

  private void parseStocks(String stocks) {
    Arrays.stream(stocks.split("\n")).forEach(line -> parseStock(line));
  }

  private void parseStock(String line) {
    Pattern p = Pattern.compile("A checking account with (.+) (.+)".toUpperCase());
    Matcher m = p.matcher(line.trim().toUpperCase());
    if(!m.matches()) {
      parseStockOption(line);
      return;
    }

    String currency = m.group(1);
    BigDecimal amount = new BigDecimal(Double.valueOf(m.group(2)));
    sut = sut.with(new CheckingAccount(new Money(amount, Currency.getInstance(currency))));
  }

  private void parseStockOption(String line) {
    Pattern p = Pattern.compile("Stock options in .+ with (.+) .+ at (.+) (.+) each".toUpperCase());
    Matcher m = p.matcher(line.trim().toUpperCase());
    if(!m.matches()) {
      //parseStockOption(line);
      return;
    }

    int count = Integer.valueOf(m.group(1));
    String currency = m.group(2);
    BigDecimal amount = new BigDecimal(Double.valueOf(m.group(3)));
    sut = sut.with(new StockOption(count, new Money(amount, Currency.getInstance(currency))));
  }

  public WalletTestHelper and_exchange_rates(String rates) {
    stub.applyRates(rates);
    return this;
  }

  public WalletTestHelper when_I_consult_the_amount_in(String currency) {
    result = sut.evaluateIn((amount, to) -> {
      try {
        return converter.convertTo(amount, to);
      } catch (NoChangeRateException e) {
        e.printStackTrace();
      }
      return null;
    }, Currency.getInstance(currency));
    return this;
  }

  public WalletTestHelper then_I_should_have(Double expected) {
    Assertions.assertEquals(expected.doubleValue(), result.amount.doubleValue());
    return this;
  }

  Money result;
  ChangeRateCatalogStub stub = new ChangeRateCatalogStub();
  CurrencyConverter converter = new CurrencyConverter(stub);
  Wallet sut = new Wallet();
}
