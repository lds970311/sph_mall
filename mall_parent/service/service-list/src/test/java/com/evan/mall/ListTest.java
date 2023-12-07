package com.evan.mall;

import com.evan.mall.service.SearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ListTest {
    @Autowired
    private SearchService searchService;

    @Test
    void testUpperGoods() {
        for (long i = 21L; i <= 32; i++) {
            this.searchService.upperGoods(i);
        }
    }
}
