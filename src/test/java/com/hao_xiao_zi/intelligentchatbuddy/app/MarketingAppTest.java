package com.hao_xiao_zi.intelligentchatbuddy.app;

import cn.hutool.core.lang.UUID;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-01
 * Time: 20:45
 */
@SpringBootTest
class MarketingAppTest {

    @Resource
    private MarketingApp marketingApp;

    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        marketingApp.doChat("你们家的羽毛球怎么样", chatId);
    }
}
