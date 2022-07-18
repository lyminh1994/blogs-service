package com.minhlq.blogsservice.config.http;

import static com.minhlq.blogsservice.constant.HttpConfigConstants.CONNECTION_TIMEOUT;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.DEFAULT_KEEP_ALIVE_TIME;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.DEFAULT_POOL_SIZE;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.HTTPS_SCHEMA;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.HTTP_SCHEMA;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.IDLE_CONNECTION_WAIT_TIME;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.MAX_LOCALHOST_CONNECTIONS;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.MAX_ROUTE_CONNECTIONS;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.MAX_TOTAL_CONNECTIONS;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.REQUEST_TIMEOUT;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.SOCKET_TIMEOUT;
import static com.minhlq.blogsservice.constant.HttpConfigConstants.THREAD_NAME_PREFIX;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

/**
 * This class holds http client configuration settings for this application.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
@Configuration
@EnableScheduling
public class HttpClientConfig {

  @Value("${server.port}")
  private String port;

  /**
   * A bean to config connection pool ensures that already opened connections are reused.
   *
   * @return the poolingHttpClientConnectionManager
   */
  @Bean
  public PoolingHttpClientConnectionManager poolingConnectionManager() {
    PoolingHttpClientConnectionManager poolingConnectionManager = null;
    try {
      SSLContextBuilder contextBuilder = new SSLContextBuilder();
      contextBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
      Registry<ConnectionSocketFactory> socketFactoryRegistry =
          RegistryBuilder.<ConnectionSocketFactory>create()
              .register(HTTPS_SCHEMA, new SSLConnectionSocketFactory(contextBuilder.build()))
              .register(HTTP_SCHEMA, new PlainConnectionSocketFactory())
              .build();

      poolingConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
      // set total amount of connections across all HTTP routes
      poolingConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
      // set maximum amount of connections for each http route in pool
      poolingConnectionManager.setDefaultMaxPerRoute(MAX_ROUTE_CONNECTIONS);
      // increase the amounts of connections if host is localhost
      HttpHost localhost = new HttpHost("http://localhost:" + port);
      poolingConnectionManager.setMaxPerRoute(new HttpRoute(localhost), MAX_LOCALHOST_CONNECTIONS);
    } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
      log.error("Initialize Pooling Http Client Connection Manager fails", e);
    }

    return poolingConnectionManager;
  }

  /**
   * A bean determines how long a connection may remain unused in the pool until it is closed.
   *
   * @return the connectionKeepAliveStrategy
   */
  @Bean
  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy() {
    return (httpResponse, httpContext) -> {
      HeaderIterator headerIterator = httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE);
      HeaderElementIterator elementIterator = new BasicHeaderElementIterator(headerIterator);

      while (elementIterator.hasNext()) {
        HeaderElement element = elementIterator.nextElement();
        String param = element.getName();
        String value = element.getValue();
        if (value != null && param.equalsIgnoreCase("timeout")) {
          return Long.parseLong(value) * 1000; // convert to ms
        }
      }

      return DEFAULT_KEEP_ALIVE_TIME;
    };
  }

  /**
   * A bean to configure a monitor that runs every 20 seconds to close outdated connections.
   *
   * @param pool the poolingHttpClientConnectionManager
   * @return the runnable
   */
  @Bean
  public Runnable idleConnectionMonitor(PoolingHttpClientConnectionManager pool) {
    return new Runnable() {
      @Override
      @Scheduled(fixedDelay = 20000)
      public void run() {
        // only if connection pool is initialised
        if (pool != null) {
          pool.closeExpiredConnections();
          pool.closeIdleConnections(IDLE_CONNECTION_WAIT_TIME, TimeUnit.MILLISECONDS);

          log.debug("Idle connection monitor: Closing expired and idle connections");
        }
      }
    };
  }

  /**
   * Thread pool to schedule and execute tasks automatically.
   *
   * @return the threadPoolTaskScheduler
   */
  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
    scheduler.setThreadNamePrefix(THREAD_NAME_PREFIX);
    scheduler.setPoolSize(DEFAULT_POOL_SIZE);
    return scheduler;
  }

  /**
   * The HttpClient bean.
   *
   * @return the httpClient
   */
  @Bean
  public CloseableHttpClient httpClient() {
    RequestConfig requestConfig =
        RequestConfig.custom()
            .setConnectTimeout(CONNECTION_TIMEOUT)
            .setConnectionRequestTimeout(REQUEST_TIMEOUT)
            .setSocketTimeout(SOCKET_TIMEOUT)
            .build();

    return HttpClients.custom()
        .setDefaultRequestConfig(requestConfig)
        .setConnectionManager(poolingConnectionManager())
        .setKeepAliveStrategy(connectionKeepAliveStrategy())
        .build();
  }
}