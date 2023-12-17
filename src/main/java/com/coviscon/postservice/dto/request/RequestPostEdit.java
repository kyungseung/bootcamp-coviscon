package com.coviscon.postservice.dto.request;

import com.coviscon.postservice.entity.post.QnaStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPostEdit {

    private String title;
    private String content;

}
