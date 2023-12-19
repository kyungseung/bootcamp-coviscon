package com.coviscon.itemservice.dto.request;

import com.coviscon.itemservice.entity.item.Category;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RequestCreateLecture {
    private Long itemId;
    private Category category;
    private String title;
    private String content;

    @NotBlank(message = "금액은 필수 입력 값입니다.")
    @Pattern(regexp = "(?=.*[0-9])", message = "숫자로 금액을 입력해주세요")
    private int price;

    private Long teacherId;
    private String teacherName;
    private String realVideoName;
    private String savedPath;
    private int size;
    private String subVideoName;
    private String thumbnailFileName;
}
