package com.coviscon.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {


    private Long itemId;
    private Long teacherId;
    private String title;
    private String content;
    private int price;
    private Integer totalPrice;

    private String teacherName;
    private String press;
    private Boolean isDelete;
    private String category;


    public ItemDto(String title, String content, int price, Long teacherId,
                   String teacherName, String category) {
        this.title = title;
        this.content = content;
        this.price = price;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.category = category;
    }


}
