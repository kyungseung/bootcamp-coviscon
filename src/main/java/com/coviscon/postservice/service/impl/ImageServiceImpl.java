package com.coviscon.postservice.service.impl;

import com.coviscon.postservice.entity.post.Image;
import com.coviscon.postservice.repository.ImageRepository;
import com.coviscon.postservice.service.ImageService;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.UUID;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    @Value("${image.save.path}")
    private String uploadPath;
    private Path rootLocation;

    @Override
    @PostConstruct
    public void init() {
        this.rootLocation = Paths.get(uploadPath);
    }

    @Override
    @Transactional
    public Image store(MultipartFile multipartFile) throws Exception {
        try {
            if(multipartFile.isEmpty()) {
                throw new Exception("Failed to store empty file " + multipartFile.getOriginalFilename());
            }

            // 확장자 추출
            String extension = findExtension(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            // 이미지를 uploadPath에 저장
            String saveImageName = convertImageName(rootLocation.toString(), extension, multipartFile);

            Image image = Image.createImage(saveImageName, multipartFile, rootLocation);
            log.info("[ImageServiceImpl store] Image : {}", image);

            return imageRepository.save(image);
        } catch(IOException e) {
            throw new Exception("Failed to store file " + multipartFile.getOriginalFilename(), e);
        }
    }

    @Override
    public Image load(Long imageId) {
        return imageRepository.findById(imageId).get();
    }

    private String findExtension(String originalFilename) {
        String[] split = originalFilename.split("\\.");
        return "." + split[split.length - 1];
    }

    private String convertImageName(String rootLocation, String extension, MultipartFile multipartFile) throws IOException {
        File uploadDir = new File(rootLocation);

        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // uuid를 생성, 이미지명 설정해 파일 저장 후, 반환
        String saveImageName = UUID.randomUUID() + extension;
        File saveFile = new File(rootLocation, saveImageName);
        FileCopyUtils.copy(multipartFile.getBytes(), saveFile);

        return saveImageName;
    }
}
