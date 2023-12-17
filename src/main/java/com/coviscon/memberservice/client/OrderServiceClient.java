package com.coviscon.memberservice.client;

import com.coviscon.memberservice.dto.MemberResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@FeignClient(name = "order-service")
public interface OrderServiceClient {

    /**
     * 로그인 정보 뿌리기
     */
    @GetMapping("/order-service/saved/session")
    String savedSession(@ModelAttribute MemberResponseDto memberResponseDto);
}
