package com.jerolba.xbuffers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.util.Utf8;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.SampleDataFactory.Attr;
import com.jerolba.xbuffers.SampleDataFactory.Org;
import com.jerolba.xbuffers.SampleDataFactory.Type;

public class FromAvroWithGenericRecordByPosition {

    public static void main(String[] args) throws IOException {
        Stats stats = new Stats("Deserialize Avro");
        File file = new File("/tmp/organizations.avro");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            List<Org> orgs = deserialize(file);
            stats.add(sw);
            assert (orgs.size() == 400_000);
        }
        stats.printStat();
        histogram(file);
    }

    public static List<Org> deserialize(File file) throws IOException {
        DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();
        try (DataFileReader<GenericRecord> dataFileReader = new DataFileReader<>(file, datumReader)) {
            Schema attributes = dataFileReader.getSchema().getField("attributes").schema().getElementType();
            int idPos = attributes.getField("id").pos();
            int quantityPos = attributes.getField("quantity").pos();
            int amountPos = attributes.getField("amount").pos();
            int activePos = attributes.getField("active").pos();
            int percentPos = attributes.getField("percent").pos();
            int sizePos = attributes.getField("size").pos();
            Schema orgs = dataFileReader.getSchema();
            int namePos = orgs.getField("name").pos();
            int categoryPos = orgs.getField("category").pos();
            int countryPos = orgs.getField("country").pos();
            int organizationTypePos = orgs.getField("organizationType").pos();
            List<Org> organizations = new ArrayList<>();
            while (dataFileReader.hasNext()) {
                GenericRecord record = dataFileReader.next();
                List<GenericRecord> attrsRecords = (List<GenericRecord>) record.get("attributes");
                var attrs = attrsRecords.stream().map(attr -> new Attr(attr.get(idPos).toString(),
                        ((Integer) attr.get(quantityPos)).byteValue(),
                        ((Integer) attr.get(amountPos)).byteValue(),
                        (boolean) attr.get(activePos),
                        (double) attr.get(percentPos),
                        ((Integer) attr.get(sizePos)).shortValue())).toList();
                Utf8 name = (Utf8) record.get(namePos);
                Utf8 category = (Utf8) record.get(categoryPos);
                Utf8 country = (Utf8) record.get(countryPos);
                Type type = Type.valueOf(record.get(organizationTypePos).toString());
                organizations.add(new Org(name.toString(), category.toString(), country.toString(), type, attrs));
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
