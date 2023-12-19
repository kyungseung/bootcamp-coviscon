package com.coviscon.itemservice.repository.custom;

import com.coviscon.itemservice.dto.querydsl.LectureSearchCondition;
import com.coviscon.itemservice.dto.response.MemberResponseDto;
import com.coviscon.itemservice.dto.response.ResponseLectureList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemRepositoryCustom {
    Page<ResponseLectureList> searchByCondition(LectureSearchCondition lectureSearchCondition, Pageable pageable);

    List<ResponseLectureList> tSearchByCondition(LectureSearchCondition lectureSearchCondition, MemberResponseDto member);

    int lectureTotalPage(LectureSearchCondition lectureSearchCondition, Pageable pageable);
}
