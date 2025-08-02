package com.hao_xiao_zi.intelligentchatbuddy.rag;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetriever;
import com.alibaba.cloud.ai.dashscope.rag.DashScopeDocumentRetrieverOptions;
import org.springframework.ai.chat.client.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.rag.retrieval.search.DocumentRetriever;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-02
 * Time: 17:27
 */
@Configuration
public class LoveAppRagCloudAdvisorConfig {

    @Value("${spring.ai.dashscope.api-key}")
    private String dashScopeApiKey;

    @Bean
    public Advisor loveAppRagCloudAdvisor() {
        // 调用大模型的 API
        DashScopeApi dashScopeApi = new DashScopeApi(dashScopeApiKey);
        final String KNOWLEDGE_INDEX = "恋爱大师";
        // 创建文档检索器
        DocumentRetriever retriever = new DashScopeDocumentRetriever(
                dashScopeApi,
                DashScopeDocumentRetrieverOptions.builder().withIndexName(KNOWLEDGE_INDEX).build());
        // 创建检索增强顾问
        return RetrievalAugmentationAdvisor.builder()
                .documentRetriever(retriever)
                .build();
    }
}
