/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental;

import com.google.perftools.profiles.ProfileProto;
import io.opentelemetry.experimental.internal.MethodSampleHandler;
import io.opentelemetry.experimental.internal.OverallCPULoadHandler;
import io.opentelemetry.experimental.internal.RecordedEventHandler;
import io.opentelemetry.experimental.internal.ThreadGrouper;

import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

final class HandlerRegistry {
  private static final String SCHEMA_URL = "https://opentelemetry.io/schemas/1.6.1";
  private static final String INSTRUMENTATION_NAME = "io.opentelemetry.contrib.jfr";
  private static final String INSTRUMENTATION_VERSION = "1.7.0-SNAPSHOT";

  private final List<RecordedEventHandler> mappers;

  private HandlerRegistry(List<? extends RecordedEventHandler> mappers) {
    this.mappers = new ArrayList<>(mappers);
  }

  static HandlerRegistry createDefault(LinkedBlockingQueue<ProfileProto.Profile> sendingQueue) {

    var filtered =
        List.of(
//            new ObjectAllocationInNewTLABHandler(otelMeter, grouper),
//            new ObjectAllocationOutsideTLABHandler(otelMeter, grouper),
//            new NetworkReadHandler(otelMeter, grouper),
//            new NetworkWriteHandler(otelMeter, grouper),
//            new G1GarbageCollectionHandler(otelMeter),
//            new GCHeapSummaryHandler(otelMeter),
//            new ContextSwitchRateHandler(otelMeter),
            new OverallCPULoadHandler(),
//            new ContainerConfigurationHandler(otelMeter),
//                new MethodSampleHandler(new ThreadGrouper("sampledThread"), "jdk.NativeMethodSample"),
            new MethodSampleHandler(new ThreadGrouper("sampledThread"), "jdk.ExecutionSample", sendingQueue));
//            new LongLockHandler(otelMeter, grouper));
    filtered.forEach(RecordedEventHandler::init);

    return new HandlerRegistry(filtered);
  }

  /** @return a stream of all entries in this registry. */
  Stream<RecordedEventHandler> all() {
    return mappers.stream();
  }
}
