package com.hao_xiao_zi.intelligentchatbuddy.chatmemory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hao_xiao_zi.intelligentchatbuddy.model.ChatMessage;
import com.hao_xiao_zi.intelligentchatbuddy.server.ChatMessageService;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;

/**
 * 基于MySQL持久化存储会话记录
 */
@Component
public class MysqlBasedChatMemory implements ChatMemory{

    @Resource
    private ChatMessageService chatMessageService;

    @Override
    public void add(String conversationId, Message message) {
        // 调用方法转换为ChatMessage
        ChatMessage chatMessage = convertToChatMessage(message);
        chatMessage.setConversationId(conversationId);
        boolean isOk = chatMessageService.save(chatMessage);
        if (!isOk) {
            throw new RuntimeException("保存会话记录失败");
        }
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        if (CollUtil.isEmpty(messages)) {
            return;
        }
        // 调用方法转换为ChatMessage,并设置conversation_id
        List<ChatMessage> chatMessages = messages.stream().map(message -> {
            ChatMessage chatMessage = convertToChatMessage(message);
            chatMessage.setConversationId(conversationId);
            return chatMessage;
        }).toList();
        boolean isOk = chatMessageService.saveBatch(chatMessages);
        if (!isOk) {
            throw new RuntimeException("保存会话记录失败");
        }
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        // 查找conversationId的会话记录，并根据lastN参数返回创建时间倒序的lastN条记录
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId)
                .orderByDesc("create_time")
                .last("LIMIT " + lastN);
        List<ChatMessage> chatMessages = chatMessageService.list(queryWrapper);
        // 调用方法转换为Message
        return chatMessages.stream().map(this::convertToMessage).toList();
    }

    @Override
    public void clear(String conversationId) {
        // 删除conversationId的会话记录
        QueryWrapper<ChatMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("conversation_id", conversationId);
        boolean isOk = chatMessageService.remove(queryWrapper);
        if (!isOk) {
            throw new RuntimeException("删除会话记录失败");
        }
    }

    // Message转ChatMessage
    private ChatMessage convertToChatMessage(Message message) {
        return ChatMessage.builder()
                .messageType(message.getMessageType().getValue())
                .content(message.getText())
                .metadata(JSONUtil.toJsonStr(message.getMetadata())) // 改为使用 toJsonStr
                .build();
    }


    // ChatMessage转Message
    private Message convertToMessage(ChatMessage chatMessage) {
        String content = chatMessage.getContent();
        // 获取metadata并解析成Map
        Map<String, Object> metadataMap = JSONUtil.toBean(chatMessage.getMetadata(), Map.class);
        String messageType = chatMessage.getMessageType();

        switch (messageType) {
            case "user":
                return new UserMessage(content);
            case "assistant":
                return new AssistantMessage(content);
            case "system":
                return new SystemMessage(content);
            case "tool":
                return new ToolResponseMessage(List.of(),metadataMap);
        }
        return null;
    }
}