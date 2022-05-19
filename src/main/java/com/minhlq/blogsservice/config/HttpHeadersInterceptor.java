package com.minhlq.blogsservice.config;

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

@Log4j2
public class HttpHeadersInterceptor implements ClientHttpRequestInterceptor {

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
