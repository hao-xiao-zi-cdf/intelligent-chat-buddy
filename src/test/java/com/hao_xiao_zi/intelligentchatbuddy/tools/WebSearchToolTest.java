package com.hao_xiao_zi.intelligentchatbuddy.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-06
 * Time: 13:36
 */
@SpringBootTest
class WebSearchToolTest {

    @Value("${searchApi.api-key}")
    private String apiKey;

    @Test
    void searchWeb() {
        WebSearchTool webSearchTool = new WebSearchTool(apiKey);
        String result = webSearchTool.searchWeb("java");
        Assertions.assertNotNull(result);
    }
}