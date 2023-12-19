package com.coviscon.postservice.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestImageUpload {

    private Long imageId;
    private String imageName;
    private String saveImageName;
    private String imagePath;
    private String contentType;
    private Long size;

}
