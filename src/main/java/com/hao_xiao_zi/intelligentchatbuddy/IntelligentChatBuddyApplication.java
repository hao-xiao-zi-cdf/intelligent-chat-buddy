package com.hao_xiao_zi.intelligentchatbuddy;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.vectorstore.pgvector.autoconfigure.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hao_xiao_zi.intelligentchatbuddy.mapper")
public class IntelligentChatBuddyApplication {

    public static void main(String[] args) {
        SpringApplication.run(IntelligentChatBuddyApplication.class, args);
    }

}
