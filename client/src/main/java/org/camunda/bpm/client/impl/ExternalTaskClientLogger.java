/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.camunda.bpm.client.impl;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.camunda.bpm.client.exception.ConnectionLostException;
import org.camunda.bpm.client.exception.ExternalTaskClientException;
import org.camunda.bpm.client.exception.NotAcquiredException;
import org.camunda.bpm.client.exception.NotFoundException;
import org.camunda.bpm.client.exception.NotResumedException;
import org.camunda.bpm.client.exception.UnknownTypeException;
import org.camunda.bpm.client.exception.UnsupportedTypeException;
import org.camunda.bpm.client.topic.impl.TopicSubscriptionManagerLogger;
import org.camunda.bpm.engine.variable.value.ObjectValue;
import org.camunda.commons.logging.BaseLogger;

/**
 * @author Tassilo Weidner
 */
public class ExternalTaskClientLogger extends BaseLogger {

  protected static final String PROJECT_CODE = "CAMUNDA_EXTERNAL_TASK_CLIENT";
  protected static final String PROJECT_LOGGER = "org.camunda.bpm.client";

  public static final ExternalTaskClientLogger CLIENT_LOGGER = createLogger(ExternalTaskClientLogger.class, PROJECT_CODE, PROJECT_LOGGER, "01");

  public static final EngineClientLogger ENGINE_CLIENT_LOGGER = createLogger(EngineClientLogger.class, PROJECT_CODE, PROJECT_LOGGER, "02");

  public static final TopicSubscriptionManagerLogger TOPIC_SUBSCRIPTION_MANAGER_LOGGER = createLogger(TopicSubscriptionManagerLogger.class, PROJECT_CODE,
      PROJECT_LOGGER, "03");

  protected ExternalTaskClientException baseUrlNullException() {
    return new ExternalTaskClientException(exceptionMessage("001", "Base URL cannot be null or an empty string"));
  }

  protected ExternalTaskClientException cannotGetHostnameException() {
    return new ExternalTaskClientException(exceptionMessage("002", "Cannot get hostname"));
  }

  public ExternalTaskClientException topicNameNullException() {
    return new ExternalTaskClientException(exceptionMessage("003", "Topic name cannot be null"));
  }

  public ExternalTaskClientException lockDurationIsNotGreaterThanZeroException() {
    return new ExternalTaskClientException(exceptionMessage("004", "Lock duration is not greater than 0"));
  }

  public ExternalTaskClientException externalTaskHandlerNullException() {
    return new ExternalTaskClientException(exceptionMessage("005", "External task handler cannot be null"));
  }

  public ExternalTaskClientException topicNameAlreadySubscribedException() {
    return new ExternalTaskClientException(exceptionMessage("006", "Topic name has already been subscribed"));
  }

  public ExternalTaskClientException taskHandlerExecutorServiceNullException() {
    return new ExternalTaskClientException(exceptionMessage("007", "taskHandlerExecutorService must not be null"));
  }

  public ExternalTaskClientException externalTaskServiceException(String actionName, EngineClientException e) {
    Throwable causedException = e.getCause();

    if (causedException instanceof HttpResponseException) {
      switch (((HttpResponseException) causedException).getStatusCode()) {
      case 400:
        return new NotAcquiredException(exceptionMessage("007", "Exception while {}: The task's most recent lock could not be acquired", actionName));
      case 404:
        return new NotFoundException(exceptionMessage("008", "Exception while {}: The task could not be found", actionName));
      case 500:
        return new NotResumedException(exceptionMessage("009", "Exception while {}: The corresponding process instance could not be resumed", actionName));
      }
    }

    if (causedException instanceof ClientProtocolException || causedException instanceof IOException) {
      return new ConnectionLostException(exceptionMessage("010", "Exception while {}: Connection could not be established", actionName));
    }

    return new ExternalTaskClientException(exceptionMessage("011", "Exception while {}: '{}'", actionName));
  }

  public ExternalTaskClientException basicAuthCredentialsNullException() {
    return new ExternalTaskClientException(exceptionMessage("012", "Basic authentication credentials (username, password) cannot be null"));
  }

  protected ExternalTaskClientException interceptorNullException() {
    return new ExternalTaskClientException(exceptionMessage("013", "Interceptor cannot be null"));
  }

  public UnsupportedTypeException unsupportedTypeException(Object variableValue) {
    return new UnsupportedTypeException(
        exceptionMessage("014", "Exception while converting variable value '{}' to typed variable value: no suitable mapper found for type {}", variableValue,
            variableValue.getClass().getSimpleName()));
  }

  public UnsupportedTypeException unsupportedSerializationDataFormat(Object variableValue) {
    return new UnsupportedTypeException(
        exceptionMessage("015", "Exception while converting variable value '{}' to typed variable value: serialization data format not supported",
            variableValue, variableValue.getClass().getSimpleName()));
  }

  public UnsupportedTypeException missingSpinXmlDependencyException() {
    return new UnsupportedTypeException(exceptionMessage("016",
        "Exception while deserializing object value of type 'xml': the dependency 'camunda-spin-dataformat-xml-dom' needs to be added"));
  }

  public UnknownTypeException unknownTypeException(ObjectValue objectValue) {
    return new UnknownTypeException(
        exceptionMessage("017", "Exception while serializing variable of type object: the type of the object is not on the class path: '{}'", objectValue));
  }

  public ExternalTaskClientException maxTasksNotGreaterThanZeroException() {
    return new ExternalTaskClientException(exceptionMessage("018", "Maximum amount of fetched tasks must be greater than zero"));
  }

  public ExternalTaskClientException asyncResponseTimeoutNotGreaterThanZeroException() {
    return new ExternalTaskClientException(exceptionMessage("019", "Asynchronous response timeout must be greater than zero"));
  }

}
