package com.jerolba.xbuffers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.flat.Organization;
import com.jerolba.xbuffers.flat.Organizations;

public class FromFlatBuffersMappedFile {

    public static void main(String[] args) throws IOException {
        Stats stats = new Stats("Deserialize FlatBuffers File");
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
        int a = 0;
        int ac = 0;
        for (int i = 0; i < organizations.organizationsLength(); i++) {
            Organization organization = organizations.organizations(i);
            a += organization.name().length();
            ac += organization.attributesLength();
        }
        System.out.println("All orgs lenghts " + a + " " + ac);
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
