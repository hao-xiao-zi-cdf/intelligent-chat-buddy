package com.hao_xiao_zi.intelligentchatbuddy.controller;

import com.hao_xiao_zi.intelligentchatbuddy.agent.Manus;
import com.hao_xiao_zi.intelligentchatbuddy.app.LoveApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-08
 * Time: 15:50
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private LoveApp loveApp;

    @Resource
    private ToolCallback[] allTools;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * ai同步聊天
     * @param userPrompt 用户提示词
     * @param chatId 聊天id
     * @return 聊天结果
     */
    @GetMapping("/app/chat/sync")
    public String doChatWithLoveAppSync(String userPrompt,String chatId){
        return loveApp.doChat(userPrompt,chatId);
    }

    /**
     * ai流式输出聊天
     * @param userPrompt 用户提示
     * @param chatId 聊天id
     * @return 流式输出聊天结果
     */
    @GetMapping(value = "/app/chat/sse",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> doChatWithLoveAppSse(String userPrompt, String chatId){
        return loveApp.doChatWithSSE(userPrompt,chatId);
    }

    /**
     * 基于Server-Sent Events进行流式输出聊天
     * @param userPrompt 用户提示
     * @param chatId 聊天id
     * @return 流式输出聊天结果
     */
    @GetMapping(value = "/app/chat/server_sent_event")
    public Flux<ServerSentEvent<String>> doChatWithLoveAppServerSentEvent(String userPrompt, String chatId){
        return loveApp.doChatWithSSE(userPrompt,chatId)
                .map(chunk -> ServerSentEvent.<String>builder()
                        .data(chunk)
                        .build());
    }

    /**
     * 基于SseEmitter进行流式输出聊天
     * @param userPrompt 用户提示
     * @param chatId 聊天id
     * @return SseEmitter对象
     */
    @GetMapping(value = "/app/chat/sse_emitter")
    public SseEmitter doChatWithLoveAppSseEmitter(String userPrompt, String chatId){
        // 创建一个超时时间较长的 SseEmitter
        SseEmitter emitter = new SseEmitter(180000L);
        // 获取 Flux 数据流并直接订阅
        loveApp.doChatWithSSE(userPrompt,chatId)
                .subscribe(
                    // 处理每条消息
                    chunk -> {
                        try {
                            emitter.send(chunk);
                        } catch (Exception e) {
                            emitter.completeWithError(e);
                        }
                    },
                    // 处理错误
                    emitter::completeWithError,
                    // 处理完成
                    emitter::complete);
        // 返回 SseEmitter 对象
        return emitter;
    }

    /**
     * 流式调用 Manus 超级智能体
     *
     * @param message
     * @return
     */
    @GetMapping("/manus/chat")
    public SseEmitter doChatWithManus(String message) {
        Manus yuManus = new Manus(allTools, dashscopeChatModel);
        return yuManus.runStream(message);
    }
}
