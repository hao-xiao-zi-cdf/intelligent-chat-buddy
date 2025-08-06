package com.hao_xiao_zi.intelligentchatbuddy.tools;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-06
 * Time: 14:28
 */
@SpringBootTest
public class PDFGenerationToolTest {

    @Test
    public void testGeneratePDF() {
        PDFGenerationTool tool = new PDFGenerationTool();
        String fileName = "hi.pdf";
        String content = "Hello World";
        String result = tool.generatePDF(fileName, content);
        assertNotNull(result);
    }
}
