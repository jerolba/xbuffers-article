package com.jerolba.xbuffers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.google.common.base.Stopwatch;
import com.google.flatbuffers.FlatBufferBuilder;
import com.jerolba.xbuffers.SampleDataFactory.Attr;
import com.jerolba.xbuffers.SampleDataFactory.Org;
import com.jerolba.xbuffers.flat.Attribute;
import com.jerolba.xbuffers.flat.Organization;
import com.jerolba.xbuffers.flat.Organizations;

public class ToFlatbuffers {

    public static void main(String[] args) throws IOException {
        run(new SampleDataFactory().getOrganizations(400_000));
    }

    public static void run(List<Org> organizations) throws IOException {
        Stats stats = new Stats("Serialize flatbuffers");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            OutputStream os = new FileOutputStream("/tmp/organizations.flatbuffers");
            serialize(organizations, os);
            os.close();
            stats.add(sw);
        }
        stats.printStat();
        MemoryHisto.histo(() -> createSerializable(organizations));
    }

    public static void serialize(List<Org> organizations, OutputStream os) throws IOException {
        FlatBufferBuilder builder = createSerializable(organizations);
        InputStream sizedInputStream = builder.sizedInputStream();
        byte[] buffer = new byte[1024 * 1024];
        int read;
        while ((read = sizedInputStream.read(buffer, 0, 1024 * 1024)) >= 0) {
            os.write(buffer, 0, read);
        }
        os.close();
    }

    public static FlatBufferBuilder createSerializable(List<Org> organizations) {
        FlatBufferBuilder builder = new FlatBufferBuilder(1500 * 1024 * 1024);

        int[] orgsArr = new int[organizations.size()];
        int contOrgs = 0;
        for (Org org : organizations) {
            int[] attributes = new int[org.attributes().size()];
            int contAttr = 0;
            for (Attr attr : org.attributes()) {
                int idOffset = builder.createString(attr.id());
                attributes[contAttr++] = Attribute.createAttribute(builder, idOffset,
                        attr.quantity(), attr.amount(), attr.size(),
                        attr.percent(), attr.active());
            }
            int attrsOffset = Organization.createAttributesVector(builder, attributes);

            int nameOffset = builder.createString(org.name());
            int categoryOffset = builder.createString(org.category());
            byte type = (byte) org.type().ordinal();
            int countryOffset = builder.createString(org.country());
            orgsArr[contOrgs++] = Organization.createOrganization(builder, nameOffset,
                    categoryOffset, type, countryOffset, attrsOffset);
        }
        int organizationsOffset = Organizations.createOrganizationsVector(builder, orgsArr);
        int root_table = Organizations.createOrganizations(builder, organizationsOffset);
        builder.finish(root_table);
        return builder;
    }
}
