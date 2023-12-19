package com.coviscon.postservice.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "item-service")
public interface ItemServiceClient {

//    @GetMapping("/item-service/{qnaId}/items")
//    List<ResponsePostList> getItems(@PathVariable Long qnaId);
}
