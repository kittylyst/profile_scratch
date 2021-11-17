/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental.profiler;

import java.util.Collections;
import java.util.List;

public final class JvmStackTrace {
  private final String threadName;
  private final String threadState;
  private final List<JvmStackFrame> frames;

  /** Creates a stack trace. */
  public JvmStackTrace(String threadName, String threadState, List<JvmStackFrame> frames) {
    this.threadName = threadName;
    this.threadState = threadState;
    this.frames = Collections.unmodifiableList(frames);
  }

  public String threadName() {
    return threadName;
  }

  public String threadState() {
    return threadState;
  }

  public List<JvmStackFrame> frames() {
    return frames;
  }

  public static final class JvmStackFrame {
    private final String desc;
    private final int line;
    private final int bytecodeIndex;

    /** Creates a stack frame. */
    public JvmStackFrame(String desc, int line, int bytecodeIndex) {
      this.desc = desc;
      this.line = line;
      this.bytecodeIndex = bytecodeIndex;
    }

    public String desc() {
      return desc;
    }

    public int line() {
      return line;
    }

    public int bytecodeIndex() {
      return bytecodeIndex;
    }
  }
}
