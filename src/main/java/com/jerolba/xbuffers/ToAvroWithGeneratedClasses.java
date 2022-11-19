package com.jerolba.xbuffers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.avro.file.DataFileWriter;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.SampleDataFactory.Org;
import com.jerolba.xbuffers.avro.Attribute;
import com.jerolba.xbuffers.avro.Organization;
import com.jerolba.xbuffers.avro.OrganizationType;

public class ToAvroWithGeneratedClasses {

    public static void main(String[] args) throws IOException {
        run(new SampleDataFactory().getOrganizations(400_000));
    }

    public static void run(List<Org> organizations) throws IOException {
        Stats stats = new Stats("Serialize Avro");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            try (OutputStream os = new FileOutputStream("/tmp/organizations.avro")) {
                serialize(organizations, os);
            }
            stats.add(sw);
        }
        stats.printStat();
    }

    public static void serialize(List<Org> organizations, OutputStream os) throws IOException {
        DatumWriter<Organization> datumWriter = new SpecificDatumWriter<>(Organization.class);
        DataFileWriter<Organization> dataFileWriter = new DataFileWriter<>(datumWriter);
        dataFileWriter.create(new Organization().getSchema(), os);
        for (var org : organizations) {
            List<Attribute> attrs = org.attributes().stream()
                    .map(a -> Attribute.newBuilder()
                            .setId(a.id())
                            .setQuantity(a.quantity())
                            .setAmount(a.amount())
                            .setSize(a.size())
                            .setPercent(a.percent())
                            .setActive(a.active())
                            .build())
                    .toList();
            Organization organization = Organization.newBuilder()
                    .setName(org.name())
                    .setCategory(org.category())
                    .setCountry(org.country())
                    .setOrganizationType(OrganizationType.valueOf(org.type().name()))
                    .setAttributes(attrs)
                    .build();
            dataFileWriter.append(organization);
        }
        dataFileWriter.close();
    }

}
