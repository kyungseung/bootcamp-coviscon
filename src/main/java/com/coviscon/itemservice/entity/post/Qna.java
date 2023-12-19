package com.coviscon.itemservice.entity.post;



import com.coviscon.itemservice.entity.auditing.BaseTimeEntity;
import com.coviscon.itemservice.entity.item.Category;
import com.coviscon.itemservice.entity.item.Item;
import com.coviscon.itemservice.entity.item.Lecture;
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

    @OneToMany(mappedBy = "qna")
    private List<Image> images = new ArrayList<>();


    // == 생성 메서드 --
//    public static Qna addPost(RequestPostCreate requestPostCreate) {
//        return Qna.builder()
//                .itemId(100L)
//                .teacherId(101L)
//                .title(requestPostCreate.getTitle())
//                .content(requestPostCreate.getContent())
////                .imageFileName(requestPostCreate.getImageFileName())
//                .qnaStatus(requestPostCreate.getQnaStatus())
//                .build();
//    }

    public static Qna addQna(String title, String content, Long memberId, String nickName, Item item) {
        Qna qna = Qna.builder()
            .title(title)
            .content(content)
            .memberId(memberId)
            .nickName(nickName)
            .qnaStatus(QnaStatus.INCOMPLETE)
            .lecture((Lecture) item)
            .build();


        return qna;
    }
}
