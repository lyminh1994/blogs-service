package com.minhlq.blogsservice.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

@Log4j2
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

  @Override
  public ClientHttpResponse intercept(
      HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
    // Logging the http request
    log.debug("URI: {}", request.getURI());
    log.debug("HTTP Method: {}", request.getMethodValue());
    log.debug("HTTP Headers: {}", request.getHeaders());
    log.debug("Request body: {}", new String(body, StandardCharsets.UTF_8));

    return execution.execute(request, body);
  }
}
