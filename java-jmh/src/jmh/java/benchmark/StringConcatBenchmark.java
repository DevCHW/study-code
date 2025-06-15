package benchmark;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/**
 * @author DevCHW
 * @since 2025-06-15
 * 문자열 연결 성능 비교: String vs StringBuilder vs StringBuffer
 * @BenchmarkMode: 벤치마크 측정 모드 설정 (전체, 처리량, 평균시간, 샘플시간, 단일실행시간)
 * @OutputTimeUnit: 결과 시간 단위 지정
 * @State: 벤치마크 인스턴스 상태 (스레드, 벤치마크, 그룹)
 * @Warmup: 준비 운전 설정 (반복 횟수, 시간 등)
 * @Measurement: 측정 횟수 및 시간 설정
 * @Fork: JVM 프로세스를 몇 번 새로 띄울지 설정
 */
@BenchmarkMode(Mode.All)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
@Fork(1)
public class StringConcatBenchmark {

    // 반복 횟수를 10만번으로 설정
    private static final int ITERATIONS = 100_000;

    /**
     * String 연산자(+)를 사용한 문자열 연결 성능 측정
     */
    @Benchmark
    public String testString() {
        String s = "";
        for (int i = 0; i < ITERATIONS; i++) {
            s += "a";
        }
        return s;
    }

    /**
     * StringBuilder를 사용한 문자열 연결 성능 측정
     */
    @Benchmark
    public String testStringBuilder() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ITERATIONS; i++) {
            sb.append("a");
        }
        return sb.toString();
    }

    /**
     * StringBuffer를 사용한 문자열 연결 성능 측정
     */
    @Benchmark
    public String testStringBuffer() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < ITERATIONS; i++) {
            sb.append("a");
        }
        return sb.toString();
    }

}
