package thalesdigital.io.wallet;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;

public class FinanceApi {

  private static final String BASE_URL = "https://api.exchangeratesapi.io";

  public String convert(String from, String to, String amount) throws IOException {
    BigDecimal rate = getConversionRate(from, to);

    BigDecimal amountB = new BigDecimal(amount);

    return amountB.multiply(rate).toString();
  }

  private BigDecimal getConversionRate(String from, String to) throws IOException {
    HttpUrl httpUrl = HttpUrl.get(BASE_URL).newBuilder().addPathSegment("latest")
        .addQueryParameter("base", from)
        .build();

    final JSONObject rates = run(httpUrl).getJSONObject("rates");
    return new BigDecimal(rates.get(to).toString());
  }

  private JSONObject run(HttpUrl httpUrl) throws IOException {

    Request request = new Request.Builder()
        .url(httpUrl)
        .build();

    Response response = new OkHttpClient().newCall(request).execute();
    return new JSONObject(response.body().string());
  }
}

