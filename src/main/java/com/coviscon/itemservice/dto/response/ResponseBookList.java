package com.coviscon.itemservice.dto.response;

import com.coviscon.itemservice.entity.item.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBookList {
    private String title;
    private String author;
    private String press;
    private String url;
    private String imageFileName;
    private Category category;

    @QueryProjection
    public ResponseBookList(String title, String author, String press, String
            url, String imageFileName, Category category) {
        this.title = title;
        this.author = author;
        this.press = press;
        this.url = url;
        this.imageFileName = imageFileName;
        this.category = category;
    }
}
