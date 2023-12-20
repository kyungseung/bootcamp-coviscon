package com.coviscon.itemservice.controller;

import com.coviscon.itemservice.dto.request.RequestCreateLecture;
import com.coviscon.itemservice.dto.response.*;
import com.coviscon.itemservice.dto.s3.*;
import com.coviscon.itemservice.service.ItemService;
import com.coviscon.itemservice.service.S3MultipartService;
import com.coviscon.itemservice.util.s3.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@RequiredArgsConstructor
@RestController
public class ItemApiController {

    private final ItemService itemService;
    private final S3MultipartService s3MultipartService;
    private final HttpSession session;

    @Value("${cloud.aws.s3.bucket}")
    private String videoBucket;

    /**
     * 강의 등록 후 멤버를 거쳐 강의 리스트 or 상세 페이지로 이동
     **/
    @PostMapping("/lecture/save")
    public ResponseEntity<?> createNewLecture(
            @ModelAttribute RequestCreateLecture requestCreateLecture,
            @RequestParam("filename") MultipartFile file,
            @RequestParam("img") MultipartFile img) throws IOException {
        log.info("[itemApiController createNewLecture] RequestCreateLecture : {}", requestCreateLecture);

        MemberResponseDto member = setMemberResponseDto();

        String fileContent = file.getOriginalFilename();
        requestCreateLecture.setRealVideoName(fileContent);
        requestCreateLecture.setThumbnailFileName(img.getOriginalFilename());

        log.info("[itemApiController createNewLecture] RequestCreateLecture2 : {}", requestCreateLecture);
        itemService.uploadImg(img);
        itemService.createNewLecture(requestCreateLecture, member);

        log.info("[itemApiController createNewLecture] file size : {}", file.getSize());
        log.info("[itemApiController createNewLecture] file byte : {}", file.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/item-service/teacher/lecture/list"));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * 강의 수정 후 member를 거쳐 강의 리스트 or 상세 페이지로 이동, PutMapping?
     **/
    @PostMapping("/lecture/modify")
    public ResponseEntity<?> updateLecture(
            @ModelAttribute RequestCreateLecture requestCreateLecture,
            @RequestParam("filename") MultipartFile file,
            @RequestParam("img") MultipartFile img) {
        log.info("[itemApiController updateLecture] RequestCreateLecture : {}", requestCreateLecture);

        MemberResponseDto member = setMemberResponseDto();

        String fileContent = file.getOriginalFilename();
        requestCreateLecture.setRealVideoName(fileContent);
        requestCreateLecture.setThumbnailFileName(img.getOriginalFilename());

        itemService.uploadImg(img);
        itemService.updateLecture(requestCreateLecture, member);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/item-service/teacher/lecture/list"));

        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /**
     * 강의 삭제
     **/
    @DeleteMapping("/lecture/{itemId}/delete")
    public ResponseEntity<?> deleteLecture(@PathVariable Long itemId) {
        log.info("[itemApiController deleteLecture] itemId : {}", itemId);

        MemberResponseDto member = setMemberResponseDto();

        itemService.deleteLecture(itemId, member);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 강의 비활성화(kafka) + 어떤 형식으로 qna와 member에 전달할까?
     **/
    @PutMapping("/lecture/{itemId}/invalid")
    public ResponseEntity<ResponseLectureDetail> invalidLecture(@PathVariable Long itemId) {
        log.info("[itemApiController invalidLecture] itemId : {}", itemId);

        MemberResponseDto member = setMemberResponseDto();

        itemService.invalidLecture(itemId, member);
        ResponseLectureDetail response = new ResponseLectureDetail();
        response.setIsDelete(true);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * order-service에 id, title, price, teacherName 제공(kafka)
     **/
    @GetMapping("/{itemId}/order")
    public ResponseEntity<ResponseLectureDetail> sendToOrder(@PathVariable Long itemId) {
        log.info("[itemApiController sendToOrder] itemId : {}", itemId);

        ResponseLectureDetail response = itemService.findLectureDetail(itemId);

//        kafkaProducer.send("item-lecture-topic", response);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * 좋아요 카운트 + 어떤 형식으로 member에 정보를 전달할까?, 유저 별 내가 누른 좋아요 강의 목록 리스트
     **/
    @PutMapping("/lecture/{itemId}/like")
    public ResponseEntity<ResponseLectureDetail> updateLikeCnt(@PathVariable Long itemId) {
        log.info("[itemApiController updateLikeCnt] itemId : {}", itemId);

        ResponseLectureDetail response = itemService.updateLikeCnt(itemId);

        return ResponseEntity.ok().body(response);
    }

    /**
     * 멀티파트 업로드 시작한다.
     * 업로드 아이디를 반환하는데, 업로드 아이디는 부분 업로드, 업로드 완료 및 중지할 때 사용된다.
     *
     * @param s3UploadInitiateDto
     * @return
     */
    @CrossOrigin
    @PostMapping("/initiate-upload")
    public S3UploadDto initiateUpload(@RequestBody S3UploadInitiateDto s3UploadInitiateDto) {
        log.info("[itemApiController initiateUpload] S3 Upload Start");

        return s3MultipartService.initiateUpload(s3UploadInitiateDto.getFileName(), videoBucket, S3Config.videoFolder);
    }

    /**
     * 부분 업로드를 위한 서명된 URL 발급 요청
     *
     * @param s3UploadSignedUrlDto
     * @return
     */
    @PostMapping("/upload-signed-url")
    public S3PresignedUrlDto getUploadSignedUrl(@RequestBody S3UploadSignedUrlDto s3UploadSignedUrlDto) {
        log.info("[itemApiController getUploadSignedUrl] S3 SignedUrl for Chunk Upload");

        return s3MultipartService.getUploadSignedUrl(s3UploadSignedUrlDto, videoBucket, S3Config.videoFolder);
    }

    /**
     * 멀티파트 업로드 완료 요청
     *
     * @param s3UploadCompleteDto
     * @return
     */
    @PostMapping("/complete-upload")
    public S3UploadResultDto completeUpload(@RequestBody S3UploadCompleteDto s3UploadCompleteDto) {
        log.info("[itemApiController completeUpload] S3 Upload Finish");

        return s3MultipartService.completeUpload(s3UploadCompleteDto, videoBucket, S3Config.videoFolder);
    }

    /**
     * 멀티파트 업로드 중지
     *
     * @param s3UploadAbortDto
     * @return
     */
    @PostMapping("/abort-upload")
    public Void abortUpload(@RequestBody S3UploadAbortDto s3UploadAbortDto) {
        log.info("[itemApiController abortUpload] S3 Upload Stop");

        s3MultipartService.abortUpload(s3UploadAbortDto, videoBucket, S3Config.videoFolder);
        return null;
    }

    private MemberResponseDto setMemberResponseDto() {
        String memberId = (String) session.getAttribute("memberId");
        String email = (String) session.getAttribute("email");
        String nickName = (String) session.getAttribute("nickName");
        String role = (String) session.getAttribute("role");

        if (memberId == null)
            return null;

        return MemberResponseDto.builder()
                .memberId(Long.valueOf(memberId))
                .email(email)
                .nickName(nickName)
                .role(role)
                .build();
    }
}
