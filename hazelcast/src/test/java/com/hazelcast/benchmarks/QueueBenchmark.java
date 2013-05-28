package com.hazelcast.benchmarks;


import com.carrotsearch.junitbenchmarks.BenchmarkOptions;
import com.carrotsearch.junitbenchmarks.BenchmarkRule;
import com.carrotsearch.junitbenchmarks.annotation.AxisRange;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkHistoryChart;
import com.carrotsearch.junitbenchmarks.annotation.BenchmarkMethodChart;
import com.carrotsearch.junitbenchmarks.annotation.LabelType;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IQueue;
import org.junit.*;
import org.junit.rules.TestRule;

@AxisRange(min = 0, max = 1)
@BenchmarkMethodChart(filePrefix = "benchmark-queue")
@BenchmarkHistoryChart(filePrefix = "benchmark-queue-history", labelWith = LabelType.CUSTOM_KEY, maxRuns = 20)
public class QueueBenchmark {
    @Rule
    public TestRule benchmarkRun = new BenchmarkRule();

    private static HazelcastInstance hazelcastInstance;
    private IQueue queue;

    @BeforeClass
    public static void beforeClass() {
        hazelcastInstance = Hazelcast.newHazelcastInstance();
    }

    @Before
    public void before(){
        queue = hazelcastInstance.getQueue("exampleQueue");
    }

    @After
    public void after(){
        queue.destroy();
    }

    @AfterClass
    public static void afterClass() {
        Hazelcast.shutdownAll();
    }

    @Test
    public void addFollowedByTake() throws Exception {
        for(int k=0;k<50000;k++){
            queue.add("foo");
            queue.take();
        }
    }
}