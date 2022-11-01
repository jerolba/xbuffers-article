package com.jerolba.xbuffers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.file.DataFileReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.specific.SpecificDatumReader;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.avro.Organization;

public class FromAvroWithGeneratedClasses {

    public static void main(String[] args) throws IOException {
        Stats stats = new Stats("Deserialize Avro");
        File file = new File("/tmp/organizations.avro");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            List<Organization> orgs = deserialize(file);
            stats.add(sw);
            assert (orgs.size() == 400_000);
        }
        stats.printStat();
        histogram(file);
    }

    public static List<Organization> deserialize(File file) throws IOException {
        DatumReader<Organization> datumReader = new SpecificDatumReader<>(Organization.class);
        try (DataFileReader<Organization> dataFileReader = new DataFileReader<>(file, datumReader)) {
            List<Organization> organizations = new ArrayList<>();
            while (dataFileReader.hasNext()) {
                organizations.add(dataFileReader.next());
            }
            return organizations;
        }
    }

    public static void histogram(File file) {
        MemoryHisto.histo(() -> {
            try {
                return deserialize(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }
}
