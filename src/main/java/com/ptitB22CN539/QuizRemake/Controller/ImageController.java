package com.ptitB22CN539.QuizRemake.Controller;

import com.ptitB22CN539.QuizRemake.DTO.APIResponse;
import com.ptitB22CN539.QuizRemake.DTO.Response.ImageResponse;
import com.ptitB22CN539.QuizRemake.Service.Image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/images")
public class ImageController {
    private final IImageService imageService;

    @PostMapping()
    public ResponseEntity<APIResponse> uploadImage(@RequestPart MultipartFile image, @RequestParam(required = false) String id) {
        ImageResponse imageResponse = this.imageService.uploadImage(image, id);
        APIResponse response = APIResponse.builder()
                .code(HttpStatus.OK.value())
                .message("SUCCESS")
                .data(imageResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
