package org.camunda.bpm.client.topic.impl;

import java.util.Map;
import java.util.concurrent.Callable;

import org.camunda.bpm.client.exception.ExternalTaskClientException;
import org.camunda.bpm.client.impl.variable.TypedValueField;
import org.camunda.bpm.client.impl.variable.TypedValues;
import org.camunda.bpm.client.impl.variable.VariableValue;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.camunda.bpm.client.task.impl.ExternalTaskImpl;

public class ExternalTaskHandlerCallable implements Callable<Void> {

  protected final ExternalTaskImpl task;
  protected final ExternalTaskHandler taskHandler;
  protected final ExternalTaskService service;
  protected final TypedValues typedValues;

  public ExternalTaskHandlerCallable(ExternalTaskImpl task, ExternalTaskHandler taskHandler, ExternalTaskService service, TypedValues typedValues) {
    this.task = task;
    this.taskHandler = taskHandler;
    this.service = service;
    this.typedValues = typedValues;
  }

  @Override
  public Void call() throws Exception {

    Map<String, TypedValueField> variables = task.getVariables();
    Map<String, VariableValue> deserializeVariables = typedValues.deserializeVariables(variables);
    task.setReceivedVariableMap(deserializeVariables);

    try {
      taskHandler.execute(task, service);
    } catch (ExternalTaskClientException e) {
      TopicSubscriptionManager.LOG.exceptionOnExternalTaskServiceMethodInvocation(e);
    } catch (Throwable e) {
      TopicSubscriptionManager.LOG.exceptionWhileExecutingExternalTaskHandler(e);
    }
    return null;
  }

}
