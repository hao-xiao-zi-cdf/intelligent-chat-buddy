package com.hao_xiao_zi.intelligentchatbuddy.app;

import com.hao_xiao_zi.intelligentchatbuddy.advisor.MySimpleLoggerAdvisor;
import com.hao_xiao_zi.intelligentchatbuddy.chatmemory.MysqlBasedChatMemory;
import com.hao_xiao_zi.intelligentchatbuddy.rag.LoveAppRagCustomAdvisorFactory;
import com.hao_xiao_zi.intelligentchatbuddy.rag.QueryRewrite;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.QuestionAnswerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
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
@Slf4j
@Component
public class LoveApp {

    // 创建ChatClient
    private ChatClient chatClient;

    private static final String SYSTEM_PROMPT = "扮演深耕恋爱心理领域的专家。开场向用户表明身份，告知用户可倾诉恋爱难题。" +
            "围绕单身、恋爱、已婚三种状态提问：单身状态询问社交圈拓展及追求心仪对象的困扰；" +
            "恋爱状态询问沟通、习惯差异引发的矛盾；已婚状态询问家庭责任与亲属关系处理的问题。" +
            "引导用户详述事情经过、对方反应及自身想法，以便给出专属解决方案。";

//    @Resource
//    private ChatMemory mysqlBasedChatMemory;
//    @Resource 注解的字段注入是在构造函数执行完成之后进行的，所以在构造函数内部访问 mysqlBasedChatMemory 时它仍然是 null

    // 构造器初始化ChatClient
    public LoveApp(ChatModel dashscopeChatModel, MysqlBasedChatMemory mysqlBasedChatMemory){
        // 创建基于内存ChatMemory,存储会话记忆
//        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        // 添加对话记忆advisor
                        new MessageChatMemoryAdvisor(mysqlBasedChatMemory),
                        // 添加自己实现日志拦截器
                        new MySimpleLoggerAdvisor()
                        // 改写Prompt
//                        new ReReadingAdvisor()
                )
                .build();
    }

    public String doChat(String userMessage, String chatId){
        ChatResponse chatResponse = chatClient.prompt()
                .user(userMessage)
                // 指定对话ID和最大检索消息数(10条)添加到提示词中
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String answer = Objects.requireNonNull(chatResponse).getResult().getOutput().getText();
        log.info("回答：{}", answer);
        return answer;
    }

    /**
     * 使用SSE进行流式输出
     * @param userMessage 用户输入
     * @param chatId 聊天ID
     * @return 流式输出结果
     */
    public Flux<String> doChatWithSSE(String userMessage, String chatId){
        return chatClient.prompt()
                .user(userMessage)
                // 指定对话ID和最大检索消息数(10条)添加到提示词中
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 1))
                .stream()
                .content();
    }

    record LoveReport(String title, List<String> suggestions) {
    }

    public LoveReport doChatWithReport(String userMessage, String chatId) {
        LoveReport entity = chatClient.prompt()
                .user(userMessage)
                .system(SYSTEM_PROMPT + "每次对话后都要生成恋爱结果，标题为{用户名}的恋爱报告，内容为建议列表")
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .entity(LoveReport.class);
        log.info("报告：{}", entity);
        return entity;
    }

    @Resource
    private VectorStore loveAppVectorStore;

    @Resource
    private Advisor loveAppRagCloudAdvisor;

    @Resource
    private VectorStore pgVectorVectorStore;

    @Resource
    private QueryRewrite queryRewrite;

    public String doChatByRag(String userMessage, String chatId){
        // 查询重写
        String userMessageRewrite = queryRewrite.rewrite(userMessage);
        String content = chatClient.prompt()
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                                .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
//                .advisors(// 应用Rag知识库问答
//                        new QuestionAnswerAdvisor(loveAppVectorStore))
//                .advisors(// 应用Rag增强检索服务（基于云知识库服务）
//                        loveAppRagCloudAdvisor)
//                .advisors(// 应用Rag增强检索服务（基于PgVector向量存储）
//                        new QuestionAnswerAdvisor(pgVectorVectorStore))
                .advisors(// 应用Rag增强检索服务（自定义）
                        LoveAppRagCustomAdvisorFactory.createLoveAppRagCustomAdvisor(loveAppVectorStore, "单身"))
                .user(userMessageRewrite)
                .call().content();
        log.info("content: {}", content);
        return content;
    }

    @Resource
    private ToolCallback[] allTools;

    public String doChatWithTools(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(allTools)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }

    @Resource
    private ToolCallbackProvider toolCallbackProvider;

    public String doChatWithMcp(String message, String chatId) {
        ChatResponse response = chatClient
                .prompt()
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .tools(toolCallbackProvider)
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}", content);
        return content;
    }
}
