package xrate;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.org.apache.xerces.internal.xs.StringList;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Provide access to basic currency exchange rate services.
 *
 * @author aaaaaaaaaa-team
 */
public class ExchangeRateReader {

    private String base;

    /**
     * Construct an exchange rate reader using the given base URL. All requests
     * will then be relative to that URL. If, for example, your source is Xavier
     * Finance, the base URL is http://api.finance.xaviermedia.com/api/ Rates
     * for specific days will be constructed from that URL by appending the
     * year, month, and day; the URL for 25 June 2010, for example, would be
     * http://api.finance.xaviermedia.com/api/2010/06/25.xml
     * 
     * @param baseURL
     *            the base URL for requests
     */
    public ExchangeRateReader(String baseURL) {
        base = baseURL;
    }

    public float getRate(JsonObject ratesInfo, String currency){
        return ratesInfo.getAsJsonObject("rates").get(currency).getAsFloat();
    }

    /**
     * Get the exchange rate for the specified currency against the base
     * currency (the Euro) on the specified date.
     * 
     * @param currencyCode
     *            the currency code for the desired currency
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(String currencyCode, int year, int month, int day) throws IOException {
        String urlString = base;
        if(urlString.contains("faculty")){
            urlString = urlString + "/" + year + "-" + month + "-" + day;
        }
        URL url = new URL(urlString);
        InputStream inputStream = url.openStream();
        Reader reader = new InputStreamReader(inputStream);
        JsonObject rawData = new JsonParser().parse(reader).getAsJsonObject();
        Float rate = getRate(rawData,currencyCode);

        return rate;
    }

    /**
     * Get the exchange rate of the first specified currency against the second
     * on the specified date.
     * 
     * @param fromCurrency
     *            the currency code we're exchanging *from*
     * @param toCurrency
     *            the currency code we're exchanging *to*
     * @param year
     *            the year as a four digit integer
     * @param month
     *            the month as an integer (1=Jan, 12=Dec)
     * @param day
     *            the day of the month as an integer
     * @return the desired exchange rate
     * @throws IOException
     */
    public float getExchangeRate(
            String fromCurrency, String toCurrency,
            int year, int month, int day) throws IOException {
        String urlString = base;
        if(urlString.contains("faculty")){
            urlString = urlString + year + "-" + month + "-" + day;
        }
        URL url = new URL(urlString);
        InputStream inputStream = url.openStream();
        Reader reader = new InputStreamReader(inputStream);
        JsonObject rawData = new JsonParser().parse(reader).getAsJsonObject();
        float rateFrom = getRate(rawData, fromCurrency);
        float rateTo = getRate(rawData, toCurrency);

        Float rate = rateFrom/rateTo;

        return rate;
    }
}