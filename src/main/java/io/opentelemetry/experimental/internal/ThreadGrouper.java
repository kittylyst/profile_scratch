/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental.internal;

import java.util.Optional;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedThread;

public final class ThreadGrouper {
  private final String threadFieldName;

  public ThreadGrouper(String threadFieldName) {
    this.threadFieldName = threadFieldName;
  }

  public Optional<String> groupedName(RecordedEvent ev) {
    if (ev.hasField(threadFieldName)) {
      Object thisField = ev.getValue(threadFieldName);
      if (thisField != null && thisField instanceof RecordedThread) {
        RecordedThread rt = (RecordedThread) thisField;
        return groupedName(rt);
      }
    } else {
      System.out.println(ev);
    }
    return Optional.empty();
  }

  // FIXME doesn't actually do any grouping, but should be safe for now
  public Optional<String> groupedName(RecordedThread rt) {
    return Optional.of(rt.getJavaName());
  }
}
