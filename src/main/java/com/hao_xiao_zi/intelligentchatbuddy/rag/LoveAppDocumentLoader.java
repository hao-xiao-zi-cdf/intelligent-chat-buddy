package com.hao_xiao_zi.intelligentchatbuddy.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-02
 * Time: 14:54
 */
@Component
@Slf4j
public class LoveAppDocumentLoader {

    // 引入资源解析器
    private final ResourcePatternResolver resourcePatternResolver;

    public LoveAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    // 加载Markdown文件
    List<Document> loadMarkdown() {
        List<Document> documents = new ArrayList<>();
        // 解析资源
        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath:document/*.md");
            for (Resource resource : resources) {
                String fileName = resource.getFilename();
                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder()
                        .withHorizontalRuleCreateDocument(true)
                        .withIncludeCodeBlock(false)
                        .withIncludeBlockquote(false)
                        .withAdditionalMetadata("filename", fileName)
                        .build();
                documents.addAll(new MarkdownDocumentReader(resource, config).read());
            }
        } catch (IOException e) {
            log.error("加载Markdown文档异常: {}", e.getMessage());
        }
        return documents;
    }

}
