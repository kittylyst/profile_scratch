package io.opentelemetry.experimental.internal;

import com.google.perftools.profiles.ProfileProto;
import io.opentelemetry.experimental.internal.profiler.JvmStackTrace;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ProfileAdapterTest {

    @Test
    public void testBuilder() {
        var trace = new JvmStackTrace("main", Thread.State.RUNNABLE.name(), List.of());
        ProfileAdapter.adapt(trace);
        assertTrue(true);
    }
}
