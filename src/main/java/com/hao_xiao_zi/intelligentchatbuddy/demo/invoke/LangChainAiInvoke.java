package com.hao_xiao_zi.intelligentchatbuddy.demo.invoke;

import dev.langchain4j.community.model.dashscope.QwenChatModel;
import dev.langchain4j.model.chat.ChatLanguageModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * LangChain4j调用千问大模型
 */
@Component
public class LangChainAiInvoke implements CommandLineRunner {

    @Value("${spring.ai.dashscope.api-key}")
    private String apiKey;

    @Override
    public void run(String... args) throws Exception {
        ChatLanguageModel qwenModel = QwenChatModel.builder()
                .apiKey(apiKey)
                .modelName("qwen-plus")
                .build();
        String answer = qwenModel.chat("我是Java小生，专门学习Java");
        System.out.println(answer);
    }
}
