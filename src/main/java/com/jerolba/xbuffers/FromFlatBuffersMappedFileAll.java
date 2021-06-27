package com.jerolba.xbuffers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.flat.Attribute;
import com.jerolba.xbuffers.flat.Organization;
import com.jerolba.xbuffers.flat.Organizations;

public class FromFlatBuffersMappedFileAll {

    public static void main(String[] args) throws IOException {
        Stats stats = new Stats("Deserialize FlatBuffers File All");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            try (RandomAccessFile file = new RandomAccessFile("/tmp/organizations.flatbuffers", "r")) {
                FileChannel inChannel = file.getChannel();
                Organizations orgs = deserialize(inChannel);
                assert (orgs.organizationsLength() == 400000);
                stats.add(sw);
                inChannel.close();
            }
        }
        stats.printStat();
        hitogram();
    }

    public static Organizations deserialize(FileChannel inChannel) throws IOException {
        MappedByteBuffer buffer = inChannel.map(MapMode.READ_ONLY, 0, inChannel.size());
        buffer.load();
        Organizations organizations = Organizations.getRootAsOrganizations(buffer);
        for (int i = 0; i < organizations.organizationsLength(); i++) {
            Organization organization = organizations.organizations(i);
            organization.name();
            organization.category();
            organization.type();
            organization.country();
            int len = organization.attributesLength();
            for (int j = 0; j < len; j++) {
                Attribute attr = organization.attributes(j);
                attr.active();
                attr.amount();
                attr.id();
                attr.percent();
                attr.quantity();
                attr.size();
            }
        }
        return organizations;
    }

    public static void hitogram() {
        MemoryHisto.histo(() -> {
            try (RandomAccessFile aFile = new RandomAccessFile("/tmp/organizations.flatbuffers", "r")) {
                FileChannel inChannel = aFile.getChannel();
                Organizations orgs = deserialize(inChannel);
                deserialize(inChannel);
                inChannel.close();
                return orgs;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

}
