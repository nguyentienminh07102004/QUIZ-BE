package com.ptitB22CN539.QuizRemake.Service.Chat;

import com.ptitB22CN539.QuizRemake.Common.Exception.DataInvalidException;
import com.ptitB22CN539.QuizRemake.Common.Exception.ExceptionVariable;
import com.ptitB22CN539.QuizRemake.DTO.Request.Chat.ChatRequestCreate;
import com.ptitB22CN539.QuizRemake.Model.Document.ChatDocument;
import com.ptitB22CN539.QuizRemake.MongoDBRepository.IChatRepository;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements IChatService {
    private final IChatRepository chatRepository;
    private final GridFsTemplate gridFsTemplate;

    @Override
    @Transactional
    public ChatDocument createChat(ChatRequestCreate chatRequestCreate) {
        ChatDocument chatDocument = new ChatDocument();
        chatDocument.setContent(chatRequestCreate.getContent());
        chatDocument.setReceiver(chatRequestCreate.getReceiver());
        return this.chatRepository.save(chatDocument);
    }

    @Override
    @Transactional
    public ObjectId saveFile(MultipartFile file) {
        try {
            return this.gridFsTemplate.store(file.getInputStream(), file.getOriginalFilename(), file.getContentType());
        } catch (Exception e) {
            throw new DataInvalidException(ExceptionVariable.FILE_TYPE_NOT_SUPPORT);
        }
    }
}
