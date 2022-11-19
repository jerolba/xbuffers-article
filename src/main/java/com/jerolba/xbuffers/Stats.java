package com.jerolba.xbuffers;

import java.util.ArrayList;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.concurrent.TimeUnit;

import com.google.common.base.Stopwatch;

public class Stats {

    private List<Long> times = new ArrayList<>();
    private String operation;

    public Stats(String operation) {
        this.operation = operation;
    }

    public void add(Stopwatch sw) {
        System.out.println(operation + ": " + sw.elapsed(TimeUnit.MILLISECONDS));
        times.add(sw.elapsed(TimeUnit.MILLISECONDS));
        try {
            System.gc();
            Thread.sleep(1000);
            System.gc();
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void printStat() {
        times.sort(Long::compare);
        LongSummaryStatistics stats = times.stream().skip(times.size() / 4).limit((times.size() * 3) / 4 - 1)
                .mapToLong(i -> i).summaryStatistics();

        System.out.println("Average time: " + stats.getAverage());
    }
}
