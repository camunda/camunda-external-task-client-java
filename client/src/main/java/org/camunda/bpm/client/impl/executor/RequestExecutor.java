package org.camunda.bpm.client.impl.executor;

import org.camunda.bpm.client.impl.EngineClientException;
import org.camunda.bpm.client.impl.RequestDto;

public interface RequestExecutor {

  <T> T postRequest(String resourceUrl, RequestDto requestDto, Class<T> responseClass) throws EngineClientException;

  byte[] getRequest(String resourceUrl) throws EngineClientException;

}
