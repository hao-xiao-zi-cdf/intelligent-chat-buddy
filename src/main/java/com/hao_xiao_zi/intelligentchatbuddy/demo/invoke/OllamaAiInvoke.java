package com.hao_xiao_zi.intelligentchatbuddy.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 本地部署ollama模型,并使用SpringAI调用
 */
@Component
public class OllamaAiInvoke implements CommandLineRunner {

    @Resource
    private ChatModel ollamaChatModel;

    @Override
    public void run(String... args) throws Exception {
        ChatResponse response = ollamaChatModel.call(new Prompt("全民制作人，你好"));
        String answer = response.getResult()
                .getOutput()
                .getText();
        System.out.println(answer);
    }
}
