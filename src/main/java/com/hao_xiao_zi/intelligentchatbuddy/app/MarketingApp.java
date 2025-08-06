package com.hao_xiao_zi.intelligentchatbuddy.app;

import com.hao_xiao_zi.intelligentchatbuddy.advisor.MySimpleLoggerAdvisor;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-01
 * Time: 20:14
 */
@Slf4j
@Component
public class MarketingApp {

    private ChatClient chatClient;

    private SystemPromptTemplate systemPromptTemplate;

    @PostConstruct
    public void init() throws IOException {
        ClassPathResource resource = new ClassPathResource("/static/prompts-templates/system-message.st");
        String templateStr = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        systemPromptTemplate = new SystemPromptTemplate(templateStr);
    }

    public MarketingApp(ChatModel dashscopeChatModel, ChatMemory mysqlBasedChatMemory){
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(
                        // 添加对话记忆advisor
                        new MessageChatMemoryAdvisor(mysqlBasedChatMemory),
                        // 添加自己实现日志拦截器
                        new MySimpleLoggerAdvisor()
                )
                .build();
    }

    public String doChat(String userMessage, String chatId){
        // 准备变量映射
        Map<String, Object> variables = new HashMap<>();
        variables.put("productName", "羽毛球");
        variables.put("features", "质量好，速度快，耐打，价格实惠");
        String systemPrompt = systemPromptTemplate.render(variables);
        String content = chatClient.prompt()
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .system(systemPrompt)
                .user(userMessage)
                .call()
                .content();
        log.info("content: {}", content);
        return content;
    }
}
