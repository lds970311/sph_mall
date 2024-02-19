package com.evan.mall.stream;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author EvanLee
 * @Description: 函数式接口测试
 * @create: 2024/2/19
 */
@SuppressWarnings("all")
public class FuctionTest {

    @Test
    public void testFunction() {
        Function<String, Integer> function = str -> Integer.parseInt(str);
        var integer = function.apply("12");
        System.out.println(integer);

        Consumer<Integer> consumer = integer1 -> System.out.println(integer1);
        consumer.accept(11);

        Supplier<Integer> supplier = () -> 10;
        Integer i = supplier.get();
        System.out.println(i);
    }
}
