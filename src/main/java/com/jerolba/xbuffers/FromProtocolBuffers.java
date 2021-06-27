package com.jerolba.xbuffers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.protocol.Organizations;

public class FromProtocolBuffers {

    public static void main(String[] args) throws IOException {
        Stats stats = new Stats("Deserialize Protocol Buffers");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            try (InputStream is = new FileInputStream("/tmp/organizations.protobuffer")) {
                Organizations orgs = deserialize(is);
                stats.add(sw);
                assert (orgs.getOrganizationsCount() == 400_000);
            }
        }
        stats.printStat();
        hitogram();
    }

    public static Organizations deserialize(InputStream is) throws IOException {
        Organizations organizations = Organizations.parseFrom(is);
        return organizations;
    }

    public static void hitogram() {
        MemoryHisto.histo(() -> {
            try (InputStream is = new FileInputStream("/tmp/organizations.protobuffer")) {
                return deserialize(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
