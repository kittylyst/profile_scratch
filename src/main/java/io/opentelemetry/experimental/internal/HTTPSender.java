package io.opentelemetry.experimental.internal;

import com.google.perftools.profiles.ProfileProto;

public interface HTTPSender {
    public void sendMsg(ProfileProto.Profile msg);
}
