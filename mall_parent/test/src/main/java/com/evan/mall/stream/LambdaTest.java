package com.evan.mall.stream;

import org.junit.jupiter.api.Test;

/**
 * @author EvanLee
 * @Description: Lambda表达式测试
 * @create: 2024/2/19
 */

@FunctionalInterface
interface LambdaInterface {
    int multi(int a, int b);

    default int add(int a, int b) {
        return a + b;
    }
}

public class LambdaTest {
    @Test
    public void testLambda() {
        LambdaInterface lambdaInterface = (a, b) -> a * b;
        System.out.println(lambdaInterface.multi(2, 3));
    }
}
