package org.camunda.bpm.client.impl.executor.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.text.SimpleDateFormat;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.camunda.bpm.client.impl.HttpComponentsRequestExecutor;
import org.camunda.bpm.client.impl.executor.RequestExecutor;
import org.camunda.bpm.client.impl.executor.RequestExecutorBuilder;
import org.camunda.bpm.client.interceptor.impl.RequestInterceptorHandler;

public class RequestExecutorBuilderHttpComponents implements RequestExecutorBuilder {

  protected RequestInterceptorHandler requestInterceptorHandler;
  protected String dateFormat;

  @Override
  public RequestExecutorBuilder withDateFormat(String dateFormat) {
    this.dateFormat = dateFormat;
    return this;
  }

  @Override
  public RequestExecutorBuilder withInterceptor(RequestInterceptorHandler requestInterceptorHandler) {
    this.requestInterceptorHandler = requestInterceptorHandler;
    return this;
  }

  @Override
  public RequestExecutor build() {
    HttpClient httpClient = initHttpClient();
    ObjectMapper objectMapper = initObjectMapper();

    return new HttpComponentsRequestExecutor(httpClient, objectMapper);
  }

  /*
   * Sub class should override this method for user defined customization
   */
  protected HttpClientBuilder customize(HttpClientBuilder httpClientBuilder) {
    return httpClientBuilder;
  }

  /*
   * Sub class should override this method for user defined customization
   */
  protected ObjectMapper customize(ObjectMapper objectMapper) {
    return objectMapper;
  }

  private HttpClient initHttpClient() {
    HttpClientBuilder httpClientBuilder = HttpClients.custom()
        .useSystemProperties();
    if (requestInterceptorHandler != null) {
      httpClientBuilder.addInterceptorLast(requestInterceptorHandler);
    }

    return customize(httpClientBuilder).build();
  }

  private ObjectMapper initObjectMapper() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS, false);
    objectMapper.configure(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE, false);

    SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    objectMapper.setDateFormat(sdf);
    objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

    return customize(objectMapper);
  }

}
