package com.coviscon.memberservice.entity.member;

import lombok.Getter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Getter
@Embeddable
//@DynamicInsert
//@DynamicUpdate
public class Mentor {

    private String job; // 직무

    private String career; // 경력

    private String company; // 현직

    private String introduce; // 멘토, 멘토링 소개

    private String mPrice; // 멘토링 가격
}
