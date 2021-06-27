package com.jerolba.xbuffers;

import java.util.function.Supplier;

import com.jerolba.jmnemohistosyne.Histogramer;
import com.jerolba.jmnemohistosyne.MemoryHistogram;

public class MemoryHisto {

    public static <T> T histo(Supplier<T> code) {
        Histogramer histogramer = new Histogramer();
        MemoryHistogram reference = histogramer.createHistogram();

        T result = code.get();

        MemoryHistogram current = histogramer.createHistogram();
        MemoryHistogram diff = current.diff(reference);
        System.out.println(diff);
        return result;
    }
}
