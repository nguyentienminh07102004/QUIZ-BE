package com.ptitB22CN539.QuizRemake.Service.Image;

import com.ptitB22CN539.QuizRemake.DTO.Response.ImageResponse;
import com.ptitB22CN539.QuizRemake.Utils.FileGoogleDrive;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {
    @Override
    public ImageResponse uploadImage(MultipartFile file, String id) {
        if (StringUtils.hasText(id)) {
            FileGoogleDrive.deleteFileGoogleImage(id);
        }
        id = FileGoogleDrive.uploadFileGoogleDrive(file);
        return ImageResponse.builder()
                .id(id)
                .url(FileGoogleDrive.getImageLinkGoogleDrive(id))
                .build();
    }
}
