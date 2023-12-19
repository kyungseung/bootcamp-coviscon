package com.coviscon.itemservice.entity.post;

import com.coviscon.itemservice.entity.auditing.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false)
    private String nickName; // member nickName
    @Column(nullable = false)
    private String tNickName; // teacher nickName
    @Column(nullable = false, length = 3000)
    private String content;

    @Column(nullable = false)
    private Boolean isDeleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qna_id")
    private Qna qna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    // == 댓글 수정 ==
//    public void update(CommentRequestDto commentRequestDto) {
//        this.content = commentRequestDto.getContent();
//    }

    // 부모 댓글 수정
    public void updateParent(Comment parent){
        this.parent = parent;
    }
}