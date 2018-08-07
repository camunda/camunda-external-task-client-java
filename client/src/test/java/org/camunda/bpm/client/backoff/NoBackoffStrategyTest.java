package org.camunda.bpm.client.backoff;

import static org.assertj.core.api.Assertions.*;

import org.assertj.core.util.Lists;
import org.camunda.bpm.client.task.impl.ExternalTaskImpl;
import org.junit.Before;
import org.junit.Test;

public class NoBackoffStrategyTest {
  
  protected BackoffStrategy backoffStrategy;
  
  @Before
  public void setup() {
    backoffStrategy = new NoBackoffStrategy();
  }
  
  @Test
  public void dontWait() {
    long backoffTime = backoffStrategy.calculateBackoffTime();
    assertThat(backoffTime).isEqualTo(0L);
  }
  
  @Test
  public void backOffForZeroTasks() {
    backoffStrategy.reconfigure(null);
    long backoffTime = backoffStrategy.calculateBackoffTime();
    assertThat(backoffTime).isEqualTo(0L);
  }
  
  @Test
  public void backOffForOneTask() {
    backoffStrategy.reconfigure(Lists.newArrayList(new ExternalTaskImpl()));
    long backoffTime = backoffStrategy.calculateBackoffTime();
    assertThat(backoffTime).isEqualTo(0L);
  }

}
