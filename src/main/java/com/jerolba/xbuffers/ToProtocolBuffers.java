package com.jerolba.xbuffers;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.SampleDataFactory.Attr;
import com.jerolba.xbuffers.SampleDataFactory.Org;
import com.jerolba.xbuffers.protocol.Organization;
import com.jerolba.xbuffers.protocol.Organization.Attribute;
import com.jerolba.xbuffers.protocol.Organization.OrganizationType;
import com.jerolba.xbuffers.protocol.Organizations;

public class ToProtocolBuffers {

    public static void main(String[] args) throws IOException {
        run(new SampleDataFactory().getOrganizations(400_000));
    }

    public static void run(List<Org> organizations) throws IOException {
        Stats stats = new Stats("Serialize protocol buffers");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            OutputStream os = new FileOutputStream("/tmp/organizations.protobuffer");
            serialize(organizations, os);
            os.flush();
            os.close();
            stats.add(sw);
        }
        stats.printStat();
        MemoryHisto.histo(() -> createSerializable(organizations));
    }

    public static void serialize(List<Org> organizations, OutputStream os) throws IOException {
        Organizations orgsBuffer = createSerializable(organizations);
        orgsBuffer.writeTo(os);
    }

    public static Organizations createSerializable(List<Org> organizations) {
        var orgsBuilder = Organizations.newBuilder();
        for (Org org : organizations) {
            var organizationBuilder = Organization.newBuilder()
                    .setName(org.name())
                    .setCategory(org.category())
                    .setCountry(org.country())
                    .setType(OrganizationType.forNumber(org.type().ordinal()));
            for (Attr attr : org.attributes()) {
                var attribute = Attribute.newBuilder()
                        .setId(attr.id())
                        .setQuantity(attr.quantity())
                        .setAmount(attr.amount())
                        .setActive(attr.active())
                        .setPercent(attr.percent())
                        .setSize(attr.size())
                        .build();
                organizationBuilder.addAttributes(attribute);
            }
            orgsBuilder.addOrganizations(organizationBuilder.build());
        }
        return orgsBuilder.build();
    }

}
