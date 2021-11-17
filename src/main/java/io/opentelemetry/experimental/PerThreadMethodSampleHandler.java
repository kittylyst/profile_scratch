/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental;

import java.util.ArrayList;
import java.util.List;

import io.opentelemetry.experimental.profiler.JvmStackTrace;
import jdk.jfr.consumer.RecordedEvent;
import jdk.jfr.consumer.RecordedFrame;
import jdk.jfr.consumer.RecordedMethod;
import jdk.jfr.consumer.RecordedStackTrace;

public class PerThreadMethodSampleHandler implements RecordedEventHandler {
  private static final String STATE = "state";
  private static final String THREAD_NAME = "thread.name";
  private static final String SAMPLED_THREAD = "sampledThread";

  private final String threadName;
  private final String eventName;

  public PerThreadMethodSampleHandler(String threadName, String eventName) {
    this.threadName = threadName;
    this.eventName = eventName;
  }

  @Override
  public PerThreadMethodSampleHandler init() {
    return this;
  }

  @Override
  public String getEventName() {
    return eventName;
  }

  @Override
  public void accept(RecordedEvent ev) {
    var trace = ev.getStackTrace();
    if (trace == null) {
      return;
    }

    //    Optional<String> threadName = Optional.empty();
    //    if (ev.hasField(SAMPLED_THREAD)) {
    //      var sampledThread = ev.getThread(SAMPLED_THREAD);
    //      threadName = grouper.groupedName(sampledThread);
    //    }

    String threadState = null;
    if (ev.hasField(STATE)) {
      threadState = ev.getString(STATE);
    }

    // FIXME Protobuf code goes here...
//    flamegraph.record(convertTrace(threadName, threadState, ev.getStackTrace()));
  }

  private static JvmStackTrace convertTrace(
      String name, String state, RecordedStackTrace stackTrace) {
    List<JvmStackTrace.JvmStackFrame> out = new ArrayList<>();

    for (RecordedFrame frame : stackTrace.getFrames()) {
      String desc = describeMethod(frame.getMethod());
      int line = frame.getLineNumber();
      int bytecodeIndex = frame.getBytecodeIndex();
      out.add(new JvmStackTrace.JvmStackFrame(desc, line, bytecodeIndex));
    }

    return new JvmStackTrace(name, state, out);
  }

  private static String describeMethod(final RecordedMethod method) {
    if (method == null) {
      return "[missing]";
    }
    String typeName = method.getType().getName();
    String methodName = method.getName();
    String descriptor = method.getDescriptor();

    // using a sized StringBuilder to prevent resizing of the internal array
    StringBuilder sb =
        new StringBuilder(typeName.length() + methodName.length() + descriptor.length() + 1);
    sb.append(typeName).append('.').append(methodName).append(descriptor);
    return sb.toString();
  }
}