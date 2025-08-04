package com.hao_xiao_zi.intelligentchatbuddy.rag;

import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 34255
 * Date: 2025-08-04
 * Time: 21:11
 */
public class LoveAppContextualQueryAugmenterFactory {

    public static ContextualQueryAugmenter createInstance() {
        PromptTemplate emptyContextPromptTemplate = new PromptTemplate("""
                你应该输出下面的内容：
                抱歉，我只能回答恋爱相关的问题，别的没办法帮到您哦，
                有问题可以联系编程导航客服 https://codefather.cn
                """);
        return ContextualQueryAugmenter.builder()
                .allowEmptyContext(false)
                // 默认不允许上下文为空，上下文为空时，使用礼貌性的语言拒绝回答，也可以使用模板，引导ai生成内容
                // 允许为空时，ai会生成通用的回复
                .emptyContextPromptTemplate(emptyContextPromptTemplate)
                .build();
    }
}
