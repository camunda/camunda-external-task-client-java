package org.camunda.bpm.client.impl.executor;

import org.camunda.bpm.client.interceptor.impl.RequestInterceptorHandler;

public interface RequestExecutorBuilder {

  /**
   * Specifies the date format to de-/serialize date variables.
   * This information is optional. Default is "yyyy-MM-dd'T'HH:mm:ss.SSSZ".
   *
   * @param dateFormat date format to be used
   * @return the builder
   */
  RequestExecutorBuilder withDateFormat(String dataFormat);

  RequestExecutorBuilder withInterceptor(RequestInterceptorHandler requestInterceptorHandler);

  RequestExecutor build();
}
