package com.ptitB22CN539.QuizRemake.MongoDBRepository;

import com.ptitB22CN539.QuizRemake.Model.Document.ChatDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRepository extends MongoRepository<ChatDocument, String> {
}
