package thalesdigital.io.wallet;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import thalesdigital.io.wallet.api.ChangeRateAccesser;
import thalesdigital.io.wallet.api.ChangeRateCatalog;
import thalesdigital.io.wallet.api.NoChangeRateException;
import thalesdigital.io.wallet.domain.exchangerate.ChangeRate;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;


class RaisingExc implements BiFunction<String, String, String> {

  @Override
  public String apply(String s, String s2) {
    throw new RuntimeException();
  }

  @Override
  public <V> BiFunction<String, String, V> andThen(Function<? super String, ? extends V> after) {
    return null;
  }
}

class Spy implements BiFunction<String, String, String> {

  @Override
  public String apply(String s, String s2) {
    boolean called = true;
    return "12";
  }

  @Override
  public <V> BiFunction<String, String, V> andThen(Function<? super String, ? extends V> after) {
    return null;
  }

  boolean called() {
    return called;
  }

  boolean called = false;
}

public class ChangeRateAccesserTest {
  @Test
  @DisplayName("Should get the right exchange rate if exists")
  void test1() throws NoChangeRateException {
    ChangeRateAccesser accs = new ChangeRateCatalog((from, to) -> "12");
    ChangeRate rate = accs.changeRate(Currency.getInstance(Locale.FRANCE), Currency.getInstance(Locale.US));
    Assertions.assertEquals(rate.from(), Currency.getInstance(Locale.FRANCE));
    Assertions.assertEquals(rate.to(), Currency.getInstance(Locale.US));
    Assertions.assertEquals(rate.getRate().setScale(2, BigDecimal.ROUND_CEILING), new BigDecimal(12).setScale(2, BigDecimal.ROUND_CEILING));
  }

  @Test
  @DisplayName("Should raise exception if could not get the exchange rate")
  void test2() {
    ChangeRateAccesser accs = new ChangeRateCatalog(new RaisingExc());
    try {
      accs.changeRate(Currency.getInstance(Locale.FRANCE), Currency.getInstance(Locale.US));
      Assertions.fail("Should raise an exception if could not get rate");
    } catch (NoChangeRateException e) {
    } catch (Exception e) {
      Assertions.fail("Should raise an NoChangeRateException if could not get rate");
    }
  }

  @Test
  @DisplayName("Should not call API if from and to are the same")
  void test3() throws NoChangeRateException {
    Spy spy = new Spy();
    ChangeRateAccesser accs = new ChangeRateCatalog(spy);
    ChangeRate r = accs.changeRate(Currency.getInstance(Locale.FRANCE), Currency.getInstance(Locale.FRANCE));
    Assertions.assertFalse(spy.called());
  }



}
