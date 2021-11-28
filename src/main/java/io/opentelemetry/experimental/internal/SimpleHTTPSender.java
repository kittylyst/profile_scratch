package io.opentelemetry.experimental.internal;

import com.google.perftools.profiles.ProfileProto;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.WRITE;

public class SimpleHTTPSender implements HTTPSender, Runnable {

    private volatile boolean shutdown = false;
    private final BlockingQueue<ProfileProto.Profile> forSending;

    public SimpleHTTPSender(BlockingQueue<ProfileProto.Profile> forSending) {
        this.forSending = forSending;
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                var msg = forSending.poll(5, TimeUnit.SECONDS);
                sendMsg(msg);
            } catch (InterruptedException e) {
                shutdown = true;
            }
        }
    }

    public void sendMsg(ProfileProto.Profile msg) {
        System.out.println("Sending...");
        try {
            var fName = "pprof-"+ System.currentTimeMillis() +".gz";
//            Files.createFile(file);

            try (var output = new FileOutputStream(fName);
                 var stream = new GZIPOutputStream(output)) {
                stream.write(msg.toByteArray());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(msg);
//        var client = HttpClient.newBuilder().build();
//        URI uri = null;
//        try {
//            uri = new URI("http://localhost:");
//            var request = HttpRequest.newBuilder(uri).build();
//
//            var response = client.send(request,
//                    HttpResponse.BodyHandlers.ofString(Charset.defaultCharset()));
//
//            System.out.println(response.body());
//        } catch (URISyntaxException | IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    public void shutdown() {
        shutdown = true;
    }
}
