package api.binance;


import static api.binance.impl.BinanceApiServiceGenerator.getSharedClient;

import api.binance.impl.BinanceApiAsyncRestClientImpl;
import api.binance.impl.BinanceApiRestClientImpl;
import api.binance.impl.BinanceApiWebSocketClientImpl;

/**
 * A factory for creating BinanceApi client objects.
 */
public class BinanceApiClientFactory {

  /**
   * API Key
   */
  private String apiKey;

  /**
   * Secret.
   */
  private String secret;

  /**
   * Instantiates a new binance api client factory.
   *
   * @param apiKey the API key
   * @param secret the Secret
   */
  private BinanceApiClientFactory(String apiKey, String secret) {
    this.apiKey = apiKey;
    this.secret = secret;
  }

  /**
   * New instance.
   *
   * @param apiKey the API key
   * @param secret the Secret
   *
   * @return the binance api client factory
   */
  public static BinanceApiClientFactory newInstance(String apiKey, String secret) {
    return new BinanceApiClientFactory(apiKey, secret);
  }

  /**
   * New instance without authentication.
   *
   * @return the binance api client factory
   */
  public static BinanceApiClientFactory newInstance() {
    return new BinanceApiClientFactory(null, null);
  }

  /**
   * Creates a new synchronous/blocking REST client.
   */
  public BinanceApiRestClient newRestClient() {
    return new BinanceApiRestClientImpl(apiKey, secret);
  }

  /**
   * Creates a new asynchronous/non-blocking REST client.
   */
  public BinanceApiAsyncRestClient newAsyncRestClient() {return new BinanceApiAsyncRestClientImpl(apiKey, secret);
  }

  /**
   * Creates a new web socket client used for handling data streams.
   */
  public BinanceApiWebSocketClient newWebSocketClient() {
    return new BinanceApiWebSocketClientImpl(getSharedClient());
  }
}