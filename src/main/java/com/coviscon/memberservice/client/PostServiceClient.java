package com.coviscon.memberservice.client;

import com.coviscon.memberservice.dto.MemberResponseDto;
import com.coviscon.memberservice.dto.client.ResponsePostDetail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "post-service")
public interface PostServiceClient {

    /**
     * 로그인 정보 뿌리기
     */
    @GetMapping("/post-service/saved/session")
    String savedSession(@ModelAttribute MemberResponseDto memberResponseDto);

    /**
     * Member QnA 정보 가져 오기
     */
    @GetMapping("/post-service/member/qna/list")
    List<ResponsePostDetail> searchMemberQna(@RequestParam Long memberId);

    /**
     * Teacher QnA 정보 가져 오기
     */
    @GetMapping("/post-service/teacher/qna/list")
    List<ResponsePostDetail> searchTeacherQna(@RequestParam Long memberId);
}
