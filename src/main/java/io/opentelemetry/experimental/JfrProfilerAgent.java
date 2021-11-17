/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

import io.opentelemetry.experimental.internal.RecordedEventHandler;
import jdk.jfr.EventSettings;
import jdk.jfr.consumer.RecordingStream;

/** The entry point class for the JFR-over-OpenTelemetry support. */
public final class JfrProfilerAgent {
  private JfrProfilerAgent() {}

  private static final Logger logger = Logger.getLogger(JfrProfilerAgent.class.getName());

  public static void agentmain(String agentArgs, Instrumentation inst) {
    premain(agentArgs, inst);
  }

  public static void premain(String agentArgs, Instrumentation inst) {
    System.out.println("Enabling agent...");
    enable();
  }

  /**
   * Enables and starts a JFR recording stream on a background thread. The thread converts a subset
   * of JFR events to pprof samples and sends them to a pprofextension HTTP endpoint.
   *
   */
  public static void enable() {
    var jfrMonitorService = Executors.newSingleThreadExecutor();
    var toMetricRegistry = HandlerRegistry.createDefault();

    jfrMonitorService.submit(
        () -> {
          try (var recordingStream = new RecordingStream()) {
            var enableMappedEvent = eventEnablerFor(recordingStream);
            toMetricRegistry.all().forEach(enableMappedEvent);
            recordingStream.setReuse(false);
            logger.log(Level.FINE, "Starting recording stream...");
            recordingStream.start(); // run forever
          }
        });
  }

  private static Consumer<RecordedEventHandler> eventEnablerFor(RecordingStream recordingStream) {
    return handler -> {
      EventSettings eventSettings = recordingStream.enable(handler.getEventName());
      handler.getPollingDuration().ifPresent(eventSettings::withPeriod);
      handler.getThreshold().ifPresent(eventSettings::withThreshold);
      recordingStream.onEvent(handler.getEventName(), handler);
    };
  }
}
