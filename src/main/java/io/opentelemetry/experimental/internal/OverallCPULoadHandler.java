/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental.internal;

import java.time.Duration;
import java.util.Optional;
import jdk.jfr.consumer.RecordedEvent;

public final class OverallCPULoadHandler implements RecordedEventHandler {
  private static final String SIMPLE_CLASS_NAME = OverallCPULoadHandler.class.getSimpleName();
  private static final String EVENT_NAME = "jdk.CPULoad";
  private static final String JVM_USER = "jvmUser";
  private static final String JVM_SYSTEM = "jvmSystem";
  private static final String MACHINE_TOTAL = "machineTotal";

  private static final String METRIC_NAME = "runtime.jvm.cpu.utilization";
  private static final String DESCRIPTION = "CPU Utilization";

  public OverallCPULoadHandler() {
//    this.otelMeter = otelMeter;
  }

  @Override
  public OverallCPULoadHandler init() {

    return this;
  }

  @Override
  public void accept(RecordedEvent ev) {
    if (ev.hasField(JVM_USER)) {
      System.out.println("User CPU: "+ ev.getDouble(JVM_USER));
    }
//    if (ev.hasField(JVM_SYSTEM)) {
//      systemHistogram.record(ev.getDouble(JVM_SYSTEM));
//    }
//    if (ev.hasField(MACHINE_TOTAL)) {
//      machineHistogram.record(ev.getDouble(MACHINE_TOTAL));
//    }
  }

  @Override
  public String getEventName() {
    return EVENT_NAME;
  }

  @Override
  public Optional<Duration> getPollingDuration() {
    return Optional.of(Duration.ofSeconds(1));
  }
}
