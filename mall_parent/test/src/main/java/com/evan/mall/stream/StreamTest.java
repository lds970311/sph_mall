package com.evan.mall.stream;

import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @author EvanLee
 * @Description: Stream API测试
 * @create: 2024/2/19
 */
public class StreamTest {
    private static Integer getInteger(List<Integer> integers) {
        Integer integer = integers.stream()
                .filter(item -> item % 2 == 0)
                .max(Integer::compareTo)
                .get();
        return integer;
    }

    @Test
    public void testStream() {
        List<Integer> integers = List.of(1, 2, 3, 4, 8, 12, 34, 55, 22);
        Integer integer = getInteger(integers);
        System.out.println(integer);
    }
}
