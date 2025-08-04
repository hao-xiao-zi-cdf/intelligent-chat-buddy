package com.hao_xiao_zi.intelligentchatbuddy.rag;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ai.rag.Query;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-04
 * Time: 15:31
 */
@SpringBootTest
class MultiQueryExpanderDemoTest {

    @Resource
    private MultiQueryExpanderDemo multiQueryExpander;

    @Test
    void multiQueryExpander() {
        List<Query> queries = multiQueryExpander.multiQueryExpander();
        Assertions.assertNotNull(queries);
    }
}