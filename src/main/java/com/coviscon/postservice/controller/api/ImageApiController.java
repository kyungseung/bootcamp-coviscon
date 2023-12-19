package com.coviscon.postservice.controller.api;

import com.coviscon.postservice.entity.post.Image;
import com.coviscon.postservice.service.ImageService;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ImageApiController {

    private final ImageService imageService;
    private final ResourceLoader resourceLoader;
    private final Environment env;

    /*
    *   이미지 업로드
    */
    @PostMapping("/image")
    public ResponseEntity<String> imageUpload(
        @RequestParam MultipartFile multipartFile,
        HttpServletResponse response) {

        try {
            Image image = imageService.store(multipartFile);
            log.info("[ImageApiController imageUpload] uploadPath : {}", image.getImagePath());

            /* image id 를 cookie 에 저장 */
            Cookie cookie = new Cookie("imageId", String.valueOf(image.getId()));
            cookie.setMaxAge(60 * 60 * 24);
            cookie.setPath(env.getProperty("prefix.url") + "create");
            response.addCookie(cookie);

            return ResponseEntity.status(HttpStatus.OK)
                .body(env.getProperty("prefix.url") + image.getId() + "/image");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    /*
    *   [커뮤니티 + 특정 강의에 대해] 게시글 작성/게시글 상세보기 시, 이미지 검색
    */
    @GetMapping("/{imageId}/image")
    public ResponseEntity<?> createCommunityImage(@PathVariable Long imageId) {
        try {
            Image image = imageService.load(imageId);
            Resource resource = resourceLoader.getResource("file:" + image.getImagePath());
//            String imgUrl = "http://127.0.0.1:8000/post-service/image/" + resource.getFilename();
            return ResponseEntity.status(HttpStatus.OK).body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image not found.");
        }
    }
}
