/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.experimental.profiler;

// This should be a record, but don't want to complicate the API build :(
public final class FlameLevel {
  private final String name;
  private final int count;
  private final String parentId;
  private final String id;

  /** Represents a level in a flamegraph. */
  public FlameLevel(String name, int count, String parentId, String id) {
    this.name = name;
    this.count = count;
    this.parentId = parentId;
    this.id = id;
  }

  public String name() {
    return name;
  }

  public int count() {
    return count;
  }

  public String parentId() {
    return parentId;
  }

  public String id() {
    return id;
  }

  @Override
  public String toString() {
    return "FlameLevel{"
        + "name='"
        + name
        + '\''
        + ", count="
        + count
        + ", parentId='"
        + parentId
        + '\''
        + ", id='"
        + id
        + '\''
        + '}';
  }
}
