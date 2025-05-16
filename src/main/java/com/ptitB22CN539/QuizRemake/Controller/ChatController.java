package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Request.Chat.ChatRequestCreate;
import com.ptitB22CN539.QuizRemake.Model.Document.ChatDocument;
import com.ptitB22CN539.QuizRemake.Service.Chat.IChatService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/chats")
public class ChatController {
    private final IChatService chatService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping()
    public ResponseEntity<APIResponse> createChat(@RequestBody ChatRequestCreate chatRequestCreate) {
        ChatDocument document = this.chatService.createChat(chatRequestCreate);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(document)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/files")
    public ResponseEntity<APIResponse> createChatFiles(@RequestPart MultipartFile file) {
        ObjectId id = this.chatService.saveFile(file);
        APIResponse response = APIResponse.builder()
                .message("SUCCESS")
                .data(id.toHexString())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @MessageMapping(value = "/send-messages")
    public void createChatFiles() {

    }
}
