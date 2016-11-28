package gov.goias.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;

import java.io.IOException;
import java.util.Collections;

/**
 * @author henrique-rh
 * @since 12/01/15.
 */
public class HeaderHttpRequestInterceptor implements ClientHttpRequestInterceptor {
  private static Logger logger = LoggerFactory.getLogger(HeaderHttpRequestInterceptor.class);

  private final String headerValue;
  private final String headerName;

  public HeaderHttpRequestInterceptor(String headerName, String headerValue) {
    this.headerValue = headerValue;
    this.headerName = headerName;
  }
 
  @Override
  public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

    HttpRequestWrapper requestWrapper = new HttpRequestWrapper(request);
    requestWrapper.getHeaders().set(headerName, headerValue);
    logger.debug( "Added {} Header: Accept -> {}", headerName, MediaType.valueOf(headerValue));

    return execution.execute(requestWrapper, body);
  }
}