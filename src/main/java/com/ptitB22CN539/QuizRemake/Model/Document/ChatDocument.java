package com.ptitB22CN539.QuizRemake.Model.Document;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@Getter
@Setter
public class ChatDocument {
    @MongoId
    private String id;
    private String content;
    @CreatedBy
    private String sender;
    private String receiver;
    @CreatedDate
    private String createdDate;
}
