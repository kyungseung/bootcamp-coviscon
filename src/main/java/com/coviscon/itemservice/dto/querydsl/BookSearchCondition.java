package com.coviscon.itemservice.dto.querydsl;

import com.coviscon.itemservice.entity.item.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookSearchCondition {
    private String title;
    private String author;
    private Category category = Category.valueOf("NONE");
}
