package com.ptitB22CN539.QuizRemake.Configuration.Redis;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class RedisExpiredEventListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        String key = message.toString();
        if (key.startsWith("quizRemake:")) {

        }
    }
}
