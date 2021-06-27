package com.jerolba.xbuffers;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.SampleDataFactory.Org;

public class FromJson {

    public static void main(String[] args) throws IOException {
        Stats stats = new Stats("Deserialize Json");
        for (int i = 0; i < 1; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            try (InputStream is = new FileInputStream("/tmp/organizations.json")) {
                List<Org> orgs = deserialize(is);
                stats.add(sw);
                assert (orgs.size() == 400_000);
            }
        }
        stats.printStat();
        hitogram();
    }

    public static List<Org> deserialize(InputStream is) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(is, new TypeReference<List<Org>>() {
        });
    }

    public static void hitogram() {
        MemoryHisto.histo(() -> {
            try (InputStream is = new FileInputStream("/tmp/organizations.json")) {
                return deserialize(is);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

}
