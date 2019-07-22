package org.camunda.bpm.client.impl.executor.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Objects;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.camunda.bpm.client.impl.HttpComponentsRequestExecutor;
import org.camunda.bpm.client.impl.executor.RequestExecutor;
import org.camunda.bpm.client.impl.executor.RequestExecutorBuilder;
import org.camunda.bpm.client.interceptor.impl.RequestInterceptorHandler;

public class RequestExecutorBuilderHttpComponents implements RequestExecutorBuilder {

  protected ObjectMapper objectMapper;
  protected RequestInterceptorHandler requestInterceptorHandler;

  @Override
  public RequestExecutorBuilder withObjectMapper(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
    return this;
  }

  @Override
  public RequestExecutorBuilder withInterceptor(RequestInterceptorHandler requestInterceptorHandler) {
    this.requestInterceptorHandler = requestInterceptorHandler;
    return this;
  }

  @Override
  public RequestExecutor build() {
    Objects.requireNonNull(objectMapper);
    Objects.requireNonNull(requestInterceptorHandler);

    HttpClient httpClient = initHttpClient();
    return new HttpComponentsRequestExecutor(httpClient, objectMapper);
  }

  protected HttpClientBuilder customize(HttpClientBuilder httpClientBuilder) {
    return httpClientBuilder;
  }

  private HttpClient initHttpClient() {
    HttpClientBuilder httpClientBuilder = customize(
        HttpClients.custom()
            .useSystemProperties()
            .addInterceptorLast(requestInterceptorHandler)
    );

    return httpClientBuilder.build();
  }
}
