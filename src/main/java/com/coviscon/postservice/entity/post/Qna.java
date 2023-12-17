package com.coviscon.postservice.entity.post;

import com.coviscon.postservice.dto.request.RequestPostCreate;
import com.coviscon.postservice.dto.request.RequestPostEdit;
import com.coviscon.postservice.dto.MemberResponseDto;
import com.coviscon.postservice.entity.auditing.BaseTimeEntity;
import com.coviscon.postservice.entity.item.Item;
import com.coviscon.postservice.entity.item.Lecture;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Qna extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "qna_id")
    private Long id;

    @Column(nullable = false)
    private String title; // 게시글 제목
    @Column(nullable = false, length = 100000)
    private String content; // 게시글 내용

//    @Column(nullable = false)
//    private Long teacherId; // teacher id [primary key]
    private Long memberId; // member id [primary key]
    private String nickName; // member nickName

    @Enumerated(EnumType.STRING)
    private QnaStatus qnaStatus; // [ COMPLETE, INCOMPLETE ]

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Lecture lecture;

    @OneToMany(mappedBy = "qna", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "qna", orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Image> images = new ArrayList<>();


    // == 생성 메서드 --
    public static Qna createPost(MemberResponseDto member, RequestPostCreate requestPostCreate) {
        return Qna.builder()
                .title(requestPostCreate.getTitle())
                .content(requestPostCreate.getContent())
                .memberId(member.getMemberId())
                .nickName(member.getNickName())
                .qnaStatus(requestPostCreate.getQnaStatus())
                .build();
    }

    /** 테스트용 **/
    public static Qna addQna(String title, String content, Long memberId, String nickName, Lecture lecture) {
        Qna qna = Qna.builder()
            .title(title)
            .content(content)
            .memberId(memberId)
            .nickName(nickName)
            .qnaStatus(QnaStatus.INCOMPLETE)
            .build();

        qna.setLecture(lecture);

        return qna;
    }

    // == 연관 관계 메서드 ==
    public void setPostItem(Item item) {
        this.lecture = (Lecture) item;
        ((Lecture) item).getQnas().add(this);

    }

    // == update ==
    public void updatePost(RequestPostEdit requestPostEdit) {
        this.title = requestPostEdit.getTitle();
        this.content = requestPostEdit.getContent();
    }

}
