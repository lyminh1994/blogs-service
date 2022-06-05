package com.minhlq.blogsservice.config.http;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.lang.NonNull;

/**
 * This class allows the execution of additional code in http request.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@Log4j2
public class HttpClientHeadersInterceptor implements ClientHttpRequestInterceptor {

  @NonNull
  @Override
  public ClientHttpResponse intercept(
      @NonNull HttpRequest request, @NonNull byte[] bytes, ClientHttpRequestExecution execution)
      throws IOException {

    HttpRequest requestWrapper = new HttpRequestWrapper(request);
    List<MediaType> accepts = new ArrayList<>();
    requestWrapper.getHeaders().setAccept(accepts);

    return execution.execute(requestWrapper, bytes);
  }
}
