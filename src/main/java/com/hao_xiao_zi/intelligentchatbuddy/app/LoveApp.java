package com.hao_xiao_zi.intelligentchatbuddy.app;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;
import java.util.Objects;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-07-31
 * Time: 17:19
 */

@Component
public class LoveApp {

    // 创建ChatClient
    private ChatClient chatClient;

    private static final Logger log = LoggerFactory.getLogger(LoveApp.class);

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

    // 构造器初始化ChatClient
    public LoveApp(ChatModel dashscopeChatModel){
        // 创建ChatMemory,存储会话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        // 添加对话记忆advisor
                        new MessageChatMemoryAdvisor(chatMemory)
                )
                .build();
    }

    public String doChat(String userMessage, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(userMessage)
                // 指定对话ID和最大检索消息数(10条)添加到提示词中
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1))
                .call()
                .chatResponse();
        String answer = Objects.requireNonNull(chatResponse).getResult().getOutput().getText();
        log.info("回答：{}", answer);
        return answer;
    }
}
