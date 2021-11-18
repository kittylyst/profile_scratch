package io.opentelemetry.experimental.internal;

import com.google.perftools.profiles.ProfileProto;
import io.opentelemetry.experimental.internal.profiler.JvmStackTrace;

import java.util.HashMap;

public class ProfileAdapter {

//    sample: A profile sample, with the values measured and the associated call stack as a list of location ids.
//    Samples with identical call stacks can be merged by adding their respective values, element by element.

//    location: A unique place in the program, commonly mapped to a single instruction address. It has a unique nonzero id,
//    to be referenced from the samples. It contains source information in the form of lines, and a mapping id that points to a binary.

//    function: A program function as defined in the program source. It has a unique nonzero id, referenced from the location lines.
//    It contains a human-readable name for the function (eg a C++ demangled name), a system name (eg a C++ mangled name), the
//    name of the corresponding source file, and other function attributes.

//    mapping: A binary that is part of the program during the profile collection. It has a unique nonzero id, referenced from
//    the locations. It includes details on how the binary was mapped during program execution. By convention the main program
//    binary is the first mapping, followed by any shared libraries.

//    string_table: All strings in the profile are represented as indices into this repeating field. The first string is empty,
//    so index == 0 always represents the empty string.

    public static ProfileProto.Profile adapt(JvmStackTrace trace) {
        var builder = ProfileProto.Profile.newBuilder();
        var hackMap = new HashMap<String, Integer>();
        hackMap.put("", 0);
        var index = 1;
        builder.addStringTable("");
        for (var frame : trace.frames()) {
            var functionBuilder = ProfileProto.Function.newBuilder();
            if (hackMap.get(frame.desc()) == null) {
                builder.addStringTable(frame.desc());
                hackMap.put(frame.desc(), index);
                functionBuilder.setName(index);
                index = index + 1;
            } else {
                functionBuilder.setName(hackMap.get(frame.desc()));
            }
            builder.addFunction(functionBuilder.build());
//            var sampleBuilder = ProfileProto.Sample.newBuilder();
//            sampleBuilder.
//            builder.setSample();
        }

        return builder.build();
    }
}
