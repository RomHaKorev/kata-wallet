package thalesdigital.io.wallet.helpers;

import thalesdigital.io.wallet.domain.Money;
import thalesdigital.io.wallet.domain.stocks.CheckingAccount;
import thalesdigital.io.wallet.domain.stocks.Stock;
import thalesdigital.io.wallet.domain.stocks.StockOption;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StockParser {
  public List<Stock> parseStocks(String stocks) {
    List<Stock> l = Arrays.stream(stocks.split("\n"))
        .filter(line -> !line.isEmpty())
        .map(line -> parseStock(line)).collect(Collectors.toList());
    return l;
  }

  private Stock parseStock(String line) {
    Pattern p = Pattern.compile("A checking account with (.+) (.+)".toUpperCase());
    Matcher m = p.matcher(line.trim().toUpperCase());
    if(!m.matches()) {
      return parseStockOption(line);
    }

    String currency = m.group(1);
    BigDecimal amount = new BigDecimal(Double.valueOf(m.group(2)));
    return new CheckingAccount(new Money(amount, Currency.getInstance(currency)));
  }

  private Stock parseStockOption(String line) {
    Pattern p = Pattern.compile("Stock options in .+ with (.+) .+ at (.+) (.+) each".toUpperCase());
    Matcher m = p.matcher(line.trim().toUpperCase());
    if(!m.matches()) {
      return null;
    }

    int count = Integer.valueOf(m.group(1));
    String currency = m.group(2);
    BigDecimal amount = new BigDecimal(Double.valueOf(m.group(3)));
    return new StockOption(count, new Money(amount, Currency.getInstance(currency)));
  }
}
