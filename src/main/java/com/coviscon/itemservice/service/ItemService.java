package com.coviscon.itemservice.service;

import com.coviscon.itemservice.dto.request.RequestCreateLecture;
import com.coviscon.itemservice.dto.response.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

public interface ItemService {
    Page<ResponseLectureList> searchLectureList(String category, String search, String keyword, Pageable pageable);
//    Page<ResponseLectureList> allLectureList(Pageable pageable);
    int lectureTotalPage(String category, String search, String keyword, Pageable pageable);
//    int defaultLectureTotalPage(Pageable pageable);
    ResponseLectureDetail findLectureDetail(Long itemId);
    Page<ResponseBookList> searchBookList(String category, String search, String keyword, Pageable pageable);
//    Page<ResponseBookList> allBookList(Pageable pageable);
    int bookTotalPage(String category, String search, String keyword, Pageable pageable);
//    int defaultBookTotalPage(Pageable pageable);
    List<ResponseLectureList> searchTeacherLectures(MemberResponseDto member, String search, String keyword);
    void deleteLecture(Long itemId, MemberResponseDto member);
    void invalidLecture(Long itemId, MemberResponseDto member);
    ResponseLectureDetail updateLikeCnt(Long itemId);
    void createNewLecture(RequestCreateLecture requestCreateLecture, MemberResponseDto member);

    void updateLecture(RequestCreateLecture requestCreateLecture, MemberResponseDto member);
    Page<ResponsePostList> searchPostKeyword(String search, String keyword, Long itemId, Pageable pageable);

    String uploadImg(MultipartFile multipartFile) throws IOException;

}
