package com.hao_xiao_zi.intelligentchatbuddy.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-04
 * Time: 15:22
 */
@Component
public class MultiQueryExpanderDemo {

    @Resource
    private ChatClient.Builder chatClientBuilder;

    public MultiQueryExpanderDemo(ChatModel deshcopeChatModel){
        this.chatClientBuilder = ChatClient.builder(deshcopeChatModel);
    }

    // 多查询扩展
    public List<Query> multiQueryExpander(){
        MultiQueryExpander queryExpander = MultiQueryExpander.builder()
                .chatClientBuilder(chatClientBuilder)
                // 生成3个查询
                .numberOfQueries(3)
                // 是否包含原始查询
                .includeOriginal(true)
                .build();
        return queryExpander.expand(new Query("我是谁，程序员吗啊啊啊"));
    }
}
