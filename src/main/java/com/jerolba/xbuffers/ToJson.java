package com.jerolba.xbuffers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.SampleDataFactory.Org;

public class ToJson {

    public static void main(String[] args) throws IOException {
        run(new SampleDataFactory().getOrganizations(400_000));
    }

    public static void run(List<Org> organizations) throws IOException {
        Stats stats = new Stats("Serialize Json");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            OutputStream os = new FileOutputStream("/tmp/organizations.json");
            serialize(organizations, os);
            os.flush();
            os.close();
            stats.add(sw);
        }
        stats.printStat();
    }

    public static void serialize(List<Org> organizations, OutputStream os) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(os, organizations);
    }

}
