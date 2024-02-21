package com.evan.mall.reactive;


import org.junit.jupiter.api.Test;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;


/**
 * @author EvanLee
 * @Description:
 * @create: 2024/2/19
 */
public class FlowApiDemo {
    @Test
    public void flowDemo1() throws InterruptedException {
        // 1. 创建发布者
        try (SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {


            // 2. 创建订阅者
            Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
                private Flow.Subscription subscription;

                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    this.subscription = subscription;
                    //从上游请求一个数据
                    subscription.request(1);
                }

                @Override
                public void onNext(String item) {
                    System.out.println("接收到数据：" + item);
                    subscription.request(1);
                }

                @Override
                public void onError(Throwable throwable) {
                    throwable.printStackTrace();
                    subscription.cancel();
                }

                @Override
                public void onComplete() {
                    System.out.println("数据接收完成");
                }
            };
            // 3. 发布者和订阅者建立订阅关系
            publisher.subscribe(subscriber);

            // 4. 生产数据
            for (int i = 0; i < 10; i++) {
                publisher.submit("数据" + i);
            }
        }
        // 5. 订阅者处理数据
        Thread.sleep(1000);
    }
}
