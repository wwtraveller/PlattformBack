package de.ait.platform.user.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UserPhotoFileDto {
    private MultipartFile photoFile;
}
