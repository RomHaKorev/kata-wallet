package thalesdigital.io.wallet.domain.stocks;

import thalesdigital.io.wallet.domain.Money;

public class CheckingAccount implements Stock {

  public CheckingAccount(Money amount) {
    this.amount = amount;
  }

  @Override
  public Money amount() {
    return amount;
  }

  final Money amount;
}
