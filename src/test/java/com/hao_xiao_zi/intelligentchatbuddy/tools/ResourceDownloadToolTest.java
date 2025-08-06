package com.hao_xiao_zi.intelligentchatbuddy.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-06
 * Time: 14:24
 */
@SpringBootTest
public class ResourceDownloadToolTest {

    @Test
    public void testDownloadResource() {
        ResourceDownloadTool tool = new ResourceDownloadTool();
        String url = "https://th.bing.com/th/id/OIP.odn5X4rz8NBhkamA5wc6QwHaEE?w=328&h=180&c=7&r=0&o=7&dpr=2&pid=1.7&rm=3";
        String fileName = "logo.png";
        String result = tool.downloadResource(url, fileName);
        assertNotNull(result);
    }
}
