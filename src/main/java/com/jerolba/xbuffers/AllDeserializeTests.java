package com.jerolba.xbuffers;

import java.io.IOException;

public class AllDeserializeTests {

    public static void main(String[] args) throws IOException {
        FromJson.main(args);
        FromProtocolBuffers.main(args);
        FromFlatBuffersMappedFile.main(args);
        FromFlatBuffersMappedFileAll.main(args);
        FromFlatBuffersMemory.main(args);
        FromFlatBuffersMemoryAll.main(args);
    }

}
