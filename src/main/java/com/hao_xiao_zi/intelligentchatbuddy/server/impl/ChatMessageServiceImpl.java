package com.hao_xiao_zi.intelligentchatbuddy.server.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hao_xiao_zi.intelligentchatbuddy.model.ChatMessage;
import com.hao_xiao_zi.intelligentchatbuddy.mapper.ChatMessageMapper;
import com.hao_xiao_zi.intelligentchatbuddy.server.ChatMessageService;
import org.springframework.stereotype.Service;

/**
* @author 34255
* @description 针对表【chat_message(聊天消息表)】的数据库操作Service实现
* @createDate 2025-08-01 15:51:12
*/
@Service
public class ChatMessageServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage>
    implements ChatMessageService {

}




