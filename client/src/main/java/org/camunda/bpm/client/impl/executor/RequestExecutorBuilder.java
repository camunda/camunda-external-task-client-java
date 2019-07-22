package org.camunda.bpm.client.impl.executor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.interceptor.impl.RequestInterceptorHandler;

public interface RequestExecutorBuilder {

  RequestExecutorBuilder withObjectMapper(ObjectMapper objectMapper);

  RequestExecutorBuilder withInterceptor(RequestInterceptorHandler requestInterceptorHandler);

  RequestExecutor build();
}
