package org.camunda.bpm.client.backoff;

import java.util.List;

import org.camunda.bpm.client.task.ExternalTask;

/**
 * No backoff. The next call is done immediately after the previous.
 * 
 * @author Ingo Richtsmeier
 *
 */
public class NoBackoffStrategy implements BackoffStrategy {

  @Override
  public void reconfigure(List<ExternalTask> externalTasks) {
  }

  @Override
  public long calculateBackoffTime() {
    return 0;
  }

}
