package thalesdigital.io.wallet.helpers;

import thalesdigital.io.wallet.domain.ChangeRate;
import thalesdigital.io.wallet.ChangeRateAccesser;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChangeRateCatalogStub implements ChangeRateAccesser {
  @Override
  public ChangeRate changeRate(Currency from, Currency to) {
    return rates.stream().filter(it -> it.to() == to && it.from() == from).findFirst().orElse(new ChangeRate(new BigDecimal(1.0D), from, to));
  }

  public void add(Currency from, Currency to, BigDecimal rate) {
    rates.add(new ChangeRate(rate, from, to));
  }

  void applyRates(String rates) {
    Arrays.stream(rates.split("\n")).forEach(
        line -> parseRate(line)
    );
  }

  private void parseRate(String line) {
    Pattern p = Pattern.compile("(.+) TO (.+) -> (.+)");
    Matcher m = p.matcher(line.trim().toUpperCase());
    if(!m.matches()) {
      return;
    }

    BigDecimal value = new BigDecimal(Double.valueOf(m.group(3)));
    value = value.setScale(2, BigDecimal.ROUND_CEILING);
    add(Currency.getInstance(m.group(1)), Currency.getInstance(m.group(2)), value);
  }

  List<ChangeRate> rates = new ArrayList<>();
}
