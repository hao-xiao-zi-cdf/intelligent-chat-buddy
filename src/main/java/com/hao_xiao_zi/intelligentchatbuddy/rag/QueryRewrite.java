package com.hao_xiao_zi.intelligentchatbuddy.rag;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.Query;
import org.springframework.ai.rag.preretrieval.query.transformation.QueryTransformer;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-04
 * Time: 15:42
 */
@Component
public class QueryRewrite {

    private QueryTransformer queryTransformer;

    public QueryRewrite(ChatModel dashscopeChatModel){
        ChatClient.Builder chatClientBuilder = ChatClient.builder(dashscopeChatModel);
        this.queryTransformer = RewriteQueryTransformer.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
    }

    // 查询重写
    public String rewrite(String queryStr){
        Query query = new Query(queryStr);
        Query transform = queryTransformer.transform(query);
        return transform.text();
    }
}
