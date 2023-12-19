package com.coviscon.postservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "member-service")
public interface MemberServiceClient {

//    @GetMapping("/member/{memberId}/test")
//    ResponseMemberQna findByMemberId(@PathVariable Long memberId);
}
