/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental.internal;

import com.google.perftools.profiles.ProfileProto;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;

public class MethodSampleHandler extends AbstractThreadDispatchingHandler {
  private static final String METRIC_NAME = "runtime.jvm.cpu.longlock.time";
  private static final String DESCRIPTION = "";

  private final String eventName;
  private final LinkedBlockingQueue<ProfileProto.Profile> sendingQueue;

  public MethodSampleHandler(ThreadGrouper grouper, String eventName, LinkedBlockingQueue<ProfileProto.Profile> sendingQueue) {
    super(grouper);
    this.eventName = eventName;
    this.sendingQueue = sendingQueue;
  }

  public enum Event {
    JAVA("jdk.ExecutionSample"),
    NATIVE("jdk.NativeMethodSample");

    private String name;

    Event(String s) {
      name = s;
    }

    public String getName() {
      return name;
    }
  }

  public MethodSampleHandler init() {
    return this;
  }

  @Override
  public String getEventName() {
    return eventName;
  }

  @Override
  public Optional<Duration> getPollingDuration() {
    return Optional.of(Duration.ofSeconds(1));
  }

  @Override
  public RecordedEventHandler createPerThreadSummarizer(String threadName) {
    var ret = new PerThreadMethodSampleHandler(threadName, eventName, sendingQueue);
    return ret.init();
  }
}
