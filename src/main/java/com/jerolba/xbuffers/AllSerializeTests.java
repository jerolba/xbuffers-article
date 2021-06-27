package com.jerolba.xbuffers;

import java.io.IOException;
import java.util.List;

import com.jerolba.xbuffers.SampleDataFactory.Org;

public class AllSerializeTests {

    public static void main(String[] args) throws IOException {
        List<Org> data = new SampleDataFactory().getOrganizations(400_000);
        ToJson.run(data);
        ToProtocolBuffers.run(data);
        ToFlatbuffers.run(data);
        ToFlatbuffersV2.run(data);
    }

}
