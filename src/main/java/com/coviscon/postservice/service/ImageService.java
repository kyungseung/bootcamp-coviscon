package com.coviscon.postservice.service;

import com.coviscon.postservice.dto.request.RequestImageUpload;
import com.coviscon.postservice.entity.post.Image;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    void init();

    Image store(MultipartFile multipartFile) throws Exception;

    Image load(Long imageId);
}
