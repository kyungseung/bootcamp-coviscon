package com.coviscon.itemservice.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseBookPage {

    private Page<ResponseBookList> bookList;
    private int startPage;
    private int endPage;

    // Constructors, getters, and setters

    public ResponseBookPage(Page<ResponseBookList> bookList, int startPage, int endPage) {
        this.bookList = bookList;
        this.startPage = startPage;
        this.endPage = endPage;
    }
}
