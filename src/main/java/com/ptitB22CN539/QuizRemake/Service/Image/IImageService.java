package com.ptitB22CN539.QuizRemake.Service.Image;

import com.ptitB22CN539.QuizRemake.DTO.Response.ImageResponse;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Nullable;

public interface IImageService {
    ImageResponse uploadImage(MultipartFile file, @Nullable String id);
}
