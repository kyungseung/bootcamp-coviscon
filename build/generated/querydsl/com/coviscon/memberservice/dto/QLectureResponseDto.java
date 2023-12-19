package com.coviscon.memberservice.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * com.coviscon.memberservice.dto.QLectureResponseDto is a Querydsl Projection type for LectureResponseDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QLectureResponseDto extends ConstructorExpression<LectureResponseDto> {

    private static final long serialVersionUID = -478870027L;

    public QLectureResponseDto(com.querydsl.core.types.Expression<String> title, com.querydsl.core.types.Expression<String> thumbnailFileName) {
        super(LectureResponseDto.class, new Class<?>[]{String.class, String.class}, title, thumbnailFileName);
    }

}

