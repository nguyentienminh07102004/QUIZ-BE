package com.ptitB22CN539.QuizRemake.Service.Chat;

import com.ptitB22CN539.QuizRemake.DTO.Request.Chat.ChatRequestCreate;
import com.ptitB22CN539.QuizRemake.Model.Document.ChatDocument;
import org.bson.types.ObjectId;
import org.springframework.web.multipart.MultipartFile;

public interface IChatService {
    ChatDocument createChat(ChatRequestCreate chatRequestCreate);
    ObjectId saveFile(MultipartFile file);
}
