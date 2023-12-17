package com.coviscon.memberservice.client;

import com.coviscon.memberservice.dto.MemberResponseDto;
import com.coviscon.memberservice.dto.client.ResponseCartDetail;
import com.coviscon.memberservice.dto.client.ResponseLectureDetail;
import com.coviscon.memberservice.dto.querydsl.LectureSearchCondition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

    /**
     * 로그인 정보 뿌리기
     */
    @GetMapping("/item-service/saved/session")
    String savedSession(@ModelAttribute MemberResponseDto memberResponseDto);

    /**
     * cart 정보 가져 오기
     */
    @GetMapping("/item-service/member/cart/list")
    List<ResponseCartDetail> searchMemberCart(@RequestParam Long memberId);

    /**
     * teacher 가 등록한 강의 조회
     */
    @GetMapping("/item-service/teacher/lectures")
    List<ResponseLectureDetail> searchTeacherLectures(
            @RequestParam Long memberId,
            @ModelAttribute LectureSearchCondition condition);
}
