package com.jerolba.xbuffers;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import com.google.common.base.Stopwatch;
import com.jerolba.xbuffers.flat.Organization;
import com.jerolba.xbuffers.flat.Organizations;

public class FromFlatBuffersMemory {

    public static void main(String[] args) throws IOException {
        Stats stats = new Stats("Deserialize FlatBuffers Memory");
        for (int i = 0; i < 10; i++) {
            Stopwatch sw = Stopwatch.createStarted();
            Organizations orgs = deserialize("/tmp/organizations.flatbuffers");
            assert (orgs.organizationsLength() == 400000);
            stats.add(sw);
        }
        stats.printStat();
        histogram();
    }

    public static Organizations deserialize(String filePath) throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(filePath, "r")) {
            FileChannel inChannel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate((int) inChannel.size());
            inChannel.read(buffer);
            inChannel.close();
            buffer.flip();
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
    }

    public static void histogram() {
        MemoryHisto.histo(() -> {
            try {
                return deserialize("/tmp/organizations.flatbuffers");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

}
