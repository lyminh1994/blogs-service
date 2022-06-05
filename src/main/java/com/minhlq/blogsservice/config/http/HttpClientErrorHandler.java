package com.minhlq.blogsservice.config.http;

import java.io.IOException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

/**
 * This class to handle the error in the middle of http request and the business logic.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
public class HttpClientErrorHandler implements ResponseErrorHandler {

  @Override
  public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
    return clientHttpResponse.getStatusCode().is4xxClientError();
  }

  @Override
  public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
    log.error(
        "CustomClientErrorHandler | HTTP Status Code: "
            + clientHttpResponse.getStatusCode().value());
  }
}
